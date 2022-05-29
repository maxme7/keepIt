let audioButtons = document.querySelectorAll(".text-to-speech");
for(let audioButton of audioButtons){
    audioButton.addEventListener("click", () => {
        document.querySelector(".play").click();
        document.querySelector(".close-modal").click();
    })
}

// clicks first play button and closes popup