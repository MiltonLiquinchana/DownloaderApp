/** Función que crea el button */
const createButton = () => {
    const buttonHTML = `
    <div class="downloadAppBtn">
        <button id="downloadBtn" class="downloadBtn" type="button">
            Dropdown button
        </button>
        <ul id="downloadQualityList" class="downloadQualityList">
            <li><a class="downloadOption" href="#">Action</a></li>
            <li><a class="downloadOption" href="#">Another action</a></li>
            <li><a class="downloadOption" href="#">Something else here</a></li>
        </ul>
    </div>`;

    const downloadAppButtonContainer = document.createElement("div");
    downloadAppButtonContainer.id = "downloadAppButtonContainer";
    downloadAppButtonContainer.innerHTML = buttonHTML;

    const downloadBtn = downloadAppButtonContainer.querySelector("#downloadBtn");

    downloadBtn.addEventListener("click", (event) => {
        const downloadQualityList = downloadAppButtonContainer.querySelector("#downloadQualityList");
        downloadQualityList.classList.toggle("show");
    });

    return downloadAppButtonContainer;

}

/**función que valida la existencia del button en el dom */
const validateButton = () => {
    let buttonExist = document.getElementById("downloadAppButtonContainer");

    return !!(buttonExist);
}

/**Función que agrega un botón al dom de YouTube */
const addButtonToDOMYouTube = (url) => {

    /**Valida si ya se a agregado el botón */
    if (validateButton()) {
        /**Si ya se agrego sale y no continua */
        return;
    }

    /**Ejecutamos la función para crear el botón, y obtenemos una instancia de la misma */
    const downloadAppButton = createButton();
    /**Por defecto para YouTube se coloca en el top 0 */
    downloadAppButton.style.top = "0px";

    /**Buscamos el reproductor de video */
    const videoContainer = document.querySelector(".html5-video-player");

    const downloadBtn = downloadAppButton.querySelector("#downloadBtn");
    downloadBtn.addEventListener("click", (event) => {
        alert(url)
    })

    /**Insertamos el botón en el videoContainer */
    videoContainer.appendChild(downloadAppButton);

}

const addButtonToDomWeb = () => {

    const mediaContainers = document.querySelectorAll("audio");

    mediaContainers.forEach((media) => {
        const button = createButton();

        const topPosition = (media.offsetTop - button.style.height.replace("px", ""));

        button.style.top = `${topPosition}px`;
        button.addEventListener("click", (event) => {
            alert(media.getAttribute("src"))
        });

        media.parentElement.appendChild(button)
    });

}
/**Función que agrega un botón en el dom de la pagina visitada */
function addButton(url) {
    /**Si pertenece a un video de youtube */
    if (url.includes("youtube.com/watch")) {
        /**Ejecuta addButtonToDOMYouTube y pasamos la url del video */
        addButtonToDOMYouTube(url);
        /** retorna para salir de la función y no continuar  */
        return;
    }

    addButtonToDomWeb();
}
