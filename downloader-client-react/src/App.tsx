import DownloaderComponent from './components/DownloaderComponent';
import DownloaderFileInfoComponent from './components/DownloaderFileInfoComponent';

function App() {

	return (
		<div>
			{/* <DownloaderFileInfoComponent /> */}

			<DownloaderComponent clientName="stompClient" />
			<DownloaderComponent clientName="stomp" />
		</div>
	);

}

export default App;
