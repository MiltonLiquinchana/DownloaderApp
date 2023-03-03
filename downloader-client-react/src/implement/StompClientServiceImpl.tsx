import webstomp, { Client } from 'webstomp-client';
import FileStatus from '../model/FileStatus';
import StompClientService from '../service/StompClientService';

export default class StompClientServiceImpl implements StompClientService {

	// Creo una variable de tipo webstomp.client
	private client: Client | null;

	private setDownloadState:Function;

	constructor(setDownloadState:Function) {

		this.client = null;

		this.setDownloadState=setDownloadState;

	}

	/** En esta función realizamos la conexión al servidor socket */
	connect(myUser: string): void {

		/** Creamos una instancia de Websocket, también se podría con stomp u otros */
		this.client = webstomp.over(new WebSocket('ws://localhost:8080/ws'));

		/** Realizamos la conexión al servidor */
		this.client.connect(
			{},
			// Esta función se ejecuta al realizar la conexión
			(frame) => {

				console.log(`Connected: ${frame}`);

				// Registramos el cliente, esto se realiza desde la función onMessage
				this.onMessage(myUser);

			},
			// Esta función se ejecuta en caso de un error
			(error) => {

				console.log('Ocurrió un problema, conectando en 10 segundos');

				console.log(error);

				/** Con un setTimeOut definimos un tiempo de espera para volver a tratar de
         * conectarnos al servidor en caso de perder la conexión */
				setTimeout(() => {

					this.connect(myUser);

				}, 10000);

			}
		);

	}

	/** función que sirve para desconectar del servidor websocket */
	disconnect(): void {

		/** Validamos que el cliente no sea null */
		if (!this.client) {

			return;

		}

		this.client.disconnect();

		console.log('Desconexión realizada');

	}

	/** Método que realiza la suscripción, para poder escuchar los mensajes que recibimos del servidor */
	onMessage(myUser: string): void {

		if (!this.client) {

			return;

		}

		/** Para recordar en el servidor definimos un endpoint para usuarios(/user), el
     * cual definimos para comunicaciones privadas */
		this.client.subscribe(`/user/${myUser}/queue/notification`, (message) => {

			const fileStatus: FileStatus = JSON.parse(message.body);

			this.setDownloadState(fileStatus.progress.toFixed(2).toString().concat('%'));

		});

	}

}
