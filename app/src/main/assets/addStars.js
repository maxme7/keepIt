let lemmaSelector = ".lemma-group .lemma-entry .col1 .lemma-pieces";
let textContentSelector = ".trans a";

let entries = document.querySelectorAll(lemmaSelector);

for(let e of entries){
    let i = document.createElement("img");
    i.src="https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg";
//    b.classList.add("text-to-speech"); //problem when screen size too small
    i.width="20";
    i.height="20";

    let injectionObject = JSON.parse(android.getInjectionObject());
    i.addEventListener('click', () => {
        android.performClick(injectionObject.field);
        android.addEntry("somestring");
    });
//    i.addEventListener('click', () => android.performClick(e.querySelector(textContentSelector).textContent));
    e.appendChild(i);
}