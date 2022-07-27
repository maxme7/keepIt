starFull = "https://www.reshot.com/preview-assets/icons/2UF67VNTWE/star-2UF67VNTWE.svg";
starEmpty = "https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg";

//yandex does not allow to load external svg files: Content Security Policy directive: "img-src 'self'

//also: again scripts are executing twice!!

//
    left = document.querySelector("div[class^=SourceLanguageToolbar-module--container]")
    left.style.display = 'flex'
    left.style.justifyContent = 'space-between'
//
    i = document.createElement("img");
        i.width="30";
        i.height="30";
        i.src = starEmpty;

    right = document.querySelector("div[class^=TargetLanguageToolbar-module--container]")
//
    j = document.createElement("img");
    j.width="30";
    j.height="30";
    j.src = starEmpty;

//german always on right side! maybe inject star to both sides?
//also no stars on "load more"
    left.appendChild(i);
    right.appendChild(j);
    console.log("hey")



//let lemmaSelector = ".lemma-group .lemma-entry .inter";  //.col1 .lemma-pieces
////let textContentSelector = ".trans a" ;
//
//let entries = document.querySelectorAll(lemmaSelector);

//for(let entry of entries){
//    let i = document.createElement("img");
//    i.width="30";
//    i.height="30";
//
//    let injectionObject = JSON.parse(android.getInjectionObject()); //currently not used TODO
//
//    //TODO can I insert svg?
//
//    function getTargetWord(){
//        let span =  entry.querySelector(".trans")
//
//        //get deepest .trans class
//        while(true){
//            let el = span.querySelector(".trans")
//            if(el != null){
//                 span = el
//            } else {
//                break
//            }
//        }
//
//        return span.textContent
//    }
//
//    function getSourceWord(){
//        let span = entry.querySelector(".mobile .lemma-pieces")
//        return span.textContent
//    }
//
//    function getSourceLang(){
//        let bodyClasses = [...document.body.classList.values()]
//        let srcClass = bodyClasses.find(el => el.startsWith( "src"))
//        return srcClass.split("-")[1]
//    }
//
//    function getTargetLang(){
//        let bodyClasses = [...document.body.classList.values()]
//        let srcClass = bodyClasses.find(el => el.startsWith( "target"))
//        return srcClass.split("-")[1]
//    }
//
//    //TODO getParentLemmaEntry() might be not specific enough (as it was with phon)
//    function getGram(){
//        let span = getParentLemmaEntry().querySelector(".gram")
//        return span != null ? span.textContent : null
//    }
//
//    function getPhon(){
//        let span = entry.querySelector(".phon")
//        return span != null ? span.textContent : null
//    }
//
//    function getInd(){
//        let span = getParentLemmaEntry().querySelector(".ind-pieces .ind")
//        if(span == null) return null
//
////        for(let s of span.querySelectorAll(".abbr")) s.remove() //removes hint box //TODO dont remove?
//        return span.textContent
//    }
//
//    function getParentLemmaEntry(){
//        return entry.closest(".lemma-entry")
//    }
//
//    function insertEntry(e){
//        e.currentTarget.src = starFull
//        e.currentTarget.removeEventListener('click', (e) => insertEntry(e))
//        e.currentTarget.addEventListener('click', (e) => deleteEntry(e))
//
//        android.insertEntry( //TODO rude!! to many arguments (?)
//            window.location.href,
//            getSourceLang(),
//            getTargetLang(),
//            getSourceWord(),
//            getTargetWord(),
//            getGram(),
//            getPhon(),
//            getInd()
//        );
//    }
//
//    function deleteEntry(e){
//        e.currentTarget.src = starEmpty
//        e.currentTarget.removeEventListener('click', (e) => deleteEntry(e))
//        e.currentTarget.addEventListener('click', (e) => insertEntry(e))
//
//        android.deleteEntry(
//            getSourceLang(),
//            getTargetLang(),
//            getSourceWord(),
//            getTargetWord(),
//            getGram(),
//            getPhon(),
//            getInd()
//        );
//    }
//
//    let doesExist = android.entryExists(getSourceLang(), getTargetLang(), getSourceWord(), getTargetWord(), getGram(), getPhon(), getInd())
//
//    if(doesExist){
//        i.src = starFull;
//        i.addEventListener('click', (e) => deleteEntry(e));
//    }else{
//        i.src = starEmpty; //star img
//        i.addEventListener('click', (e) => insertEntry(e));
//    }
//
//    entry.appendChild(i);
//}