let lemmaSelector = ".lemma-group .lemma-entry .inter";  //.col1 .lemma-pieces
let textContentSelector = ".trans a";

let entries = document.querySelectorAll(lemmaSelector);

for(let e of entries){
    let i = document.createElement("img");
    i.src="https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg"; //star img
    i.width="30";
    i.height="30";

    let injectionObject = JSON.parse(android.getInjectionObject()); //currently not used TODO

    function getTargetWord(){
        let span =  e.querySelector(".trans")

        //get deepest .trans class
        while(true){
            let el = span.querySelector(".trans")
            if(el != null){
                 span = el
            } else {
                break
            }
        }

        return span.textContent
    }

    function getSourceWord(){
        let span = e.querySelector(".mobile .lemma-pieces")
        return span.textContent
    }

    function getSourceLang(){
        let bodyClasses = [...document.body.classList.values()]
        let srcClass = bodyClasses.find(el => el.startsWith( "src"))
        return srcClass.split("-")[1]
    }

    function getTargetLang(){
        let bodyClasses = [...document.body.classList.values()]
        let srcClass = bodyClasses.find(el => el.startsWith( "target"))
        return srcClass.split("-")[1]
    }

    function getGram(){
        let span = getParentLemmaEntry().querySelector(".gram")
        return span != null ? span.textContent : null
    }

    function getPhon(){
        let span = getParentLemmaEntry().querySelector(".phon")
        return span != null ? span.textContent : null
    }

    function getInd(){
        let span = getParentLemmaEntry().querySelector(".ind-pieces")
        for(let s of span.querySelectorAll(".abbr")) s.remove() //removes hint box //TODO dont remove?
        return span != null ? span.textContent : null
    }

    function getParentLemmaEntry(){
        return e.closest(".lemma-entry")
    }

    i.addEventListener('click', () => {
        android.addEntry( //TODO rude!! to many arguments (?)
            window.location.href,
            getSourceLang(),
            getTargetLang(),
            getSourceWord(),
            getTargetWord(),
            getGram(),
            getPhon(),
            getInd()
            );
    });

    e.appendChild(i);
}