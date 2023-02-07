import { useEffect, useRef, useState } from "react";
import "../css/Downloader.css";
import StompClientServiceImpl from "../implement/StompClientServiceImpl";
import StompClientService from "../service/StompClientService";
interface props{
  clientName:string;
}

export default function DownloaderComponent({clientName}:props) {
  let stompClient: StompClientService;
  /**Creamos una referencia hacia el progres */
  const [downloadState, setDownloadState] = useState<string | number>('0%');

  /**Con useEffect vacio solo creamos una vez la instancia del cliente */
  useEffect(() => {
    stompClient = new StompClientServiceImpl(setDownloadState);
  }, []);
  /**Creamos una instancia del cliente */

  const styles = { width: downloadState };

  return (
    <div className="downloader">
      <div className="progress">
        <div
          className="progress-bar progress-bar-striped progress-bar-animated"
          role="progressbar"
          aria-label="Animated striped example"
          style={styles}
          aria-valuenow={Number(downloadState.toString().replace('%',''))}
          aria-valuemin={0}
          aria-valuemax={100}
        >
          {downloadState}
        </div>
      </div>
      <button
        type="button"
        className="btn btn-success"
        onClick={() => {
          stompClient.connect(clientName);
        }}
      >
        StartDownloader
      </button>
    </div>
  );
}
