

// adding buttons
let b = document.createElement("img");
b.src="https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg";
b.classList.add("text-to-speech");
b.width="20";
b.height="20";
document.querySelectorAll(".lemma-group .lemma-entry .col1 .lemma-pieces")[0].appendChild(b);


//add star to all
let el = document.querySelectorAll(".lemma-group .lemma-entry .col1 .lemma-pieces");
for(let e of el){
let b = document.createElement("img");
b.src="https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg";
b.classList.add("text-to-speech");
b.width="20";
b.height="20";
e.appendChild(b);
}

//cant access local files
file:///android_asset/star.svg

//svg can directly be used as html content
//SEMI COLONS!!
