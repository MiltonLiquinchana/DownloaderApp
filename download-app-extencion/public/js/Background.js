// chrome.webNavigation.onCompleted.addListener((details) => {
//     if (details.url.includes("youtube.com")) {
//         chrome.scripting.executeScript({
//             target: { tabId: details.tabId },
//             func: () => {
//                 const button = document.createElement("button");
//                 button.innerText = "Mi botón";
//                 button.style = "width: 100px; height: 50px; position:fixed; top: 100px; left: 200px; z-index: 999; background: red";
//                 const videoContainer = document.querySelector(".html5-video-player")

//                 videoContainer.appendChild(button);
//             }
//         });
//     }
// }, {
//     url: [{ hostEquals: "www.youtube.com" }]
// });


/**detecta cuando se actualiza la pagina, esto tambien sirve para cuando se visita una
 * nueva pagina web */

chrome.tabs.onUpdated.addListener((tabId, changeInfo, tab) => {

    /**Si el estado de la pagina es completo y la url de la pagina visitada no contiene
     * ninguna de las dos cadenas */
    if (changeInfo.status == "complete" && !(tab.url.includes("chrome://") || tab.url.includes("chrome-extension://"))) {
        /** Ejecuta un script javascript dentro de la pagina web */
        chrome.scripting.executeScript({
            target: { tabId: tabId },
            func: (url) => {
                /**Ejecutamos la función addButton y pasamos la url de la pagina que se visita */
                addButton(url);
            },
            args: [tab.url]
        });

    }

});

chrome.webRequest.onBeforeRequest.addListener((details) => {
    console.log(details.url)
    console.log("detalles: ")
    console.log(details)
},
    { urls: ["<all_urls>"] }
);