// Importación de la librería WebStomp para manejo de conexiones STOMP sobre WebSocket
import webstomp, { Client } from 'webstomp-client';
// Importación del modelo que representa el estado de los archivos
import FileStatus from '../model/FileStatus';
// Importación de la interfaz que define el contrato del servicio
import StompClientService from '../service/StompClientService';

/**
 * Implementación concreta del servicio cliente STOMP
 * Esta clase maneja la conexión WebSocket con el servidor usando el protocolo STOMP
 * para recibir notificaciones en tiempo real sobre el progreso de descarga de archivos
 */
export default class StompClientServiceImpl implements StompClientService {

	/**
	 * Cliente WebSocket STOMP para la comunicación con el servidor
	 * Se inicializa como null y se crea cuando se establece la conexión
	 */
	private client: Client | null = null;

	/**
	 * Constructor de la clase
	 * @param onDownloadProgressState - Callback que se ejecuta cuando se recibe 
	 *                                  una actualización del progreso de descarga
	 */
	constructor(
		public onDownloadProgressState: (downloadProgress: number) => void,
	) {
		// El constructor está vacío ya que la inicialización se hace en connect()
	}

	/**
	 * Establece la conexión con el servidor WebSocket usando protocolo STOMP
	 * @param myUser - Identificador único del usuario para las suscripciones personalizadas
	 */
	connect(myUser: string): void {

		/**
		 * Creamos una instancia del cliente STOMP sobre WebSocket
		 * El servidor debe estar corriendo en localhost:8080 con endpoint '/ws'
		 */
		this.client = webstomp.over(new WebSocket('ws://localhost:8080/ws'));

		/**
		 * Iniciamos la conexión al servidor STOMP
		 * Se pasan tres parámetros: headers, callback de éxito y callback de error
		 */
		this.client.connect(
			{}, // Headers vacíos - aquí se podrían enviar credenciales si fuera necesario
			
			/**
			 * Callback que se ejecuta cuando la conexión es exitosa
			 * @param frame - Información del frame STOMP recibido del servidor
			 */
			(frame: unknown) => {
				// Log de confirmación de conexión exitosa
				console.log(`Connected: ${frame}`);

				/**
				 * Una vez conectados, nos suscribimos a los mensajes del usuario
				 * Esto permite recibir notificaciones personalizadas
				 */
				this.onMessage(myUser);
			},
			
			/**
			 * Callback que se ejecuta cuando ocurre un error en la conexión
			 * Implementa un mecanismo de reconexión automática
			 * @param error - Información del error ocurrido
			 */
			(error: unknown) => {
				// Log del error para debugging
				console.log('Ocurrió un problema, conectando en 10 segundos');
				console.log(error);

				/**
				 * Implementación de reconexión automática con retraso
				 * Espera 10 segundos antes de intentar reconectar para evitar
				 * saturar el servidor con intentos fallidos continuos
				 */
				setTimeout(() => {
					// Llamada recursiva para reintentar la conexión
					this.connect(myUser);
				}, 10000); // 10 segundos de espera
			}
		);
	}

	/**
	 * Cierra la conexión WebSocket de manera limpia
	 * Es importante llamar este método para liberar recursos
	 */
	disconnect(): void {

		/**
		 * Verificamos que existe una conexión activa antes de intentar desconectar
		 * Esto previene errores si se llama disconnect() sin una conexión previa
		 */
		if (!this.client) {
			// No hay conexión activa, salimos temprano
			return;
		}

		// Cerramos la conexión STOMP de manera elegante
		this.client.disconnect();

		// Log de confirmación de desconexión
		console.log('Desconexión realizada');
	}

	/**
	 * Configura la suscripción para recibir mensajes del servidor
	 * Se suscribe a un canal específico del usuario para recibir notificaciones privadas
	 * @param myUser - Identificador del usuario para crear el canal personalizado
	 */
	onMessage(myUser: string): void {

		/**
		 * Verificamos que el cliente esté inicializado y conectado
		 * Sin conexión activa no podemos establecer suscripciones
		 */
		if (!this.client) {
			// No hay cliente disponible, salimos temprano
			return;
		}

		/**
		 * Nos suscribimos al canal de notificaciones específico del usuario
		 * El patrón `/user/{userId}/queue/notification` es estándar en Spring Boot
		 * para mensajes dirigidos a usuarios específicos
		 */
		this.client.subscribe(`/user/${myUser}/queue/notification`, (message: { body: string; }) => {

			/**
			 * Procesamos el mensaje recibido del servidor
			 * El cuerpo del mensaje contiene JSON con información del estado del archivo
			 */
			const fileStatus: FileStatus = JSON.parse(message.body);

			/**
			 * Extraemos el progreso y lo formateamos a 2 decimales
			 * Luego ejecutamos el callback para notificar a la UI del cambio
			 */
			this.onDownloadProgressState(Number(fileStatus.progress.toFixed(2)));
		});
	}
}