import { useEffect, useState } from 'react';

interface Props {
	url: string;
	// eslint-disable-next-line no-undef
	options?: RequestInit;
}

interface FetchState {
	data: any;
	isPending: boolean;
	requestStatus: number | null;
	error: Error | null;
}

/** En teoría este custom hook solo se devenía ejecutar una vez, solo se ejecuta cuando el
 * cuando la url, u opciones cambian
 */
export default function useFetch({ url, options }: Props): FetchState {

	/** En este estado vamos a guardar la data que recibamos al hacer la petición */
	const [data, setData] = useState<any>(null);

	/** Este estado controlara el estado de la petición, para saber si se resolvió o no la petición */
	const [isPending, setIsPending] = useState<boolean>(true);

	/** Con esto manejamos controlamos el código de estado de la petición */
	const [requestStatus, setRequestStatus] = useState<number | null>(null);

	/** Para manejar el error */
	const [error, setError] = useState<Error | null>(null);

	/** Se ejecuta cada vez que la url de la petición cambia */
	useEffect(() => {

		const getData = async () => {

			try {

				/** Asemos la petición y esperamos(await) la respuesta */
				const res = await fetch(url, options);

				/** Agregamos el código de estado de la petición */
				setRequestStatus(res.status);

				/** Validamos la respuesta */
				if (!res.ok) {

					/** Si la respuesta no es ok, retornamos este objeto, en statusText con un
					 * operador ternario decimos que si en la respuesta no existe un statusText
					 * asigne un mensaje caso contrario retorne el mensaje que retorna res */
					throw new Error(
						`${!res.statusText ? 'Ocurrió un error' : res.statusText}`
					);

				}

				/** Si no hay error, esperamos a que la respuesta se convierta a json */
				setData(await res.json());

				/** Una vez ya finalizada la conversion, actualizamos los estados de las variables */
				/** Actualizamos el estado de si esta pendiente */
				setIsPending(false);

			} catch (e) {

				setError(e as Error);

				setIsPending(false);

			}

		};

		getData();

	}, [url, options]);

	return { data, isPending, requestStatus, error };

}
