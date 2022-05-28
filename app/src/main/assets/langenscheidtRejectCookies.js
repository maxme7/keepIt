//deal with cookies langenscheidt (not needed anymore -> loaded pages are filtered"

setTimeout(function(){if(document.querySelector("#onetrust-pc-btn-handler")) document.querySelector("#onetrust-pc-btn-handler").click()}, 0);
setTimeout(function(){if(document.querySelector(".save-preference-btn-handler")) document.querySelector(".save-preference-btn-handler").click()}, 0);
document.querySelector('#search-go').addEventListener('click', () => android.performClick('pass some string value'));

document.getElementById('search-string').addEventListener('keydown', () => android.performClick());

//document.getElementById('search-string').click()
//android.performClick()