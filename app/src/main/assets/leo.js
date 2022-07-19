//website bug when info cards show up the url changes; when you search this url (which happens when you switch to panorama view) and not a clear search url then the search bar vanishes (also in firefox and chrome browser)

//also a problem is that the whole box of an entry is clickable. bad if missing star
//either other idea or just leaving leo for now

//removing top ads
document.getElementById('topBranding').remove();

//adjust search bar
document.getElementsByClassName('wgt-header')[0].remove();
document.getElementsByClassName('l-dict-searchbar')[0].style.top = '0px';

//remove ads on right side
document.getElementById('adv-skyscraper').parentElement.remove();

//remove max-width restriction in panorama view
document.firstElementChild.style.maxWidth = '100%'
document.querySelector('.l-dict-searchbar').style.maxWidth = '100%'





//moving info cards to bottom
//if(document.querySelector('#centerColumn .p-medium')){
//    //remove useless heading if pressent
//    if(document.querySelector('#centerColumn .p-medium').nextSibling.tagName == "H3"){
//        document.querySelector('#centerColumn .p-medium').nextSibling.remove()
//    }
//
//    document.querySelector('#centerColumn .p-medium').parentElement.appendChild(document.querySelector('#centerColumn .p-medium'))
//}

