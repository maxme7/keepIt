//mta = document.querySelector('meta[http-equiv="Content-Security-Policy"]')
//mta.setAttribute("content", "default-src 'self' ; img-src 'self' * data: blob: https://mc.yandex.com https://avatars-fast.yandex.net https://favicon.yandex.net https://avatars.mds.yandex.net https://s3.mds.yandex.net https://yastatic.net https://mc.admetrica.ru https://*.yandex.net:* https://yandex.ru https://yandex.com https://*.yandex.ru; font-src https://*.yandex.net:* https://yastatic.net https://www.reshot.com/*; child-src blob:; worker-src 'self' blob:; frame-src 'self' https://awaps.yandex.ru https://awaps.yandex.net https://yandexadexchange.net https://*.yandexadexchange.net https://*.yandex.ru https://yastatic.net https://z5h64q92x9.net https://*.z5h64q92x9.net:*; media-src data: https://yandex.st https://yastatic.net https://*.yandex.net; style-src 'unsafe-inline' 'unsafe-eval' https://yandex.st https://*.yandex.net:* https://yastatic.net; script-src 'self' 'unsafe-eval' 'unsafe-inline' 'nonce-oZt4sbYg7F' https://an.yandex.ru https://yandex.st https://*.yandex.net:* https://mc.yandex.ru https://yastatic.net; connect-src 'self' wss://*.yandex.net https://mc.yandex.ru https://yandex.st https://yastatic.net https://*.yandex.by https://*.yandex.com https://*.yandex.com.tr https://*.yandex.ru https://*.yandex.kz https://*.yandex.ua https://*.yandex.net https://*.yandex.net:* https://*.yandex.ru:* https://yandex.ru https://yandex.com https://mc.admetrica.ru;")

//starFull = "https://www.reshot.com/preview-assets/icons/2UF67VNTWE/star-2UF67VNTWE.svg";
//starEmpty = "https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg";

//yandex does not allow to load external svg files: Content Security Policy directive: "img-src 'self'
//so: for now just a button with text

//also: again scripts are executing twice!! not sure why since "progress below 100%" is handled

//
    left = document.querySelector(".buttons.top_buttons")
//
    i = document.createElement("button");
    i.textContent = "keepIt"


    right = document.querySelector(".buttons.buttons_translator")
//
    j = document.createElement("button");
    j.textContent = "keepIt"

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