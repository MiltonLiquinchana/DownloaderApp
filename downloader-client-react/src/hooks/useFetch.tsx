import { useEffect, useState } from "react";

interface Props {
  url: string;
  options?: RequestInit;
}

interface FetchState {
  data: any;
  isPending: boolean;
  requestStatus: number | null;
  error: Error | null;
}
export default function useFetch({ url, options }: Props): FetchState {
  /**En este estado vamos a guardar la data que recibamos al hacer la petición */
  const [data, setData] = useState<any>(null);
  /**Este estado controlara el estado de la petición, para saber si se resolvió o no la petición */
  const [isPending, setIsPending] = useState<boolean>(true);
  /**Con esto manejamos controlamos el código de estado de la peticion */
  const [requestStatus, setRequestStatus] = useState<number | null>(null);

  /**Para manejar el error */
  const [error, setError] = useState<Error | null>(null);
  /**Se ejecuta cada vez que la url de la petición cambia */
  useEffect(() => {
    const getData = async () => {
      try {
        /**Asemos la petición y esperamos(await) la respuesta */
        let res = await fetch(url, options);
        /**Agregamos el código de estado de la petición */
        setRequestStatus(res.status);
        /**Validamos la respuesta */
        if (!res.ok) {
          /**Si la respuesta no es ok, retornamos este objeto, en statusText con un
           * operador ternario decimos que si en la respuesta no existe un statusText
           * asigne un mensaje caso contrario retorne el mensaje que retorna res */
          throw new Error(
            `${!res.statusText ? "Ocurrió un error" : res.statusText}`
          );
        }
        /**Si no hay error, esperamos a que la respuesta se convierta a json*/
        let data = await res.json();
        /**Una vez ya finalizada la conversion, actualizamos los estados de las variables */
        /**Actualizamos el estado de si esta pendiente */
        setIsPending(false);
        /**Actualizamos el estado de los datos por la data que obtuvimos */
        setData(data);
      } catch (error) {
        setError(error as Error);
        setIsPending(false);
      }
    };
    getData();
  }, [url, options]);
  return { data, isPending, requestStatus, error };
}
