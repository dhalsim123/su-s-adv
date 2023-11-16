async function addToWatchlist(element){

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let titleId = element.dataset.id

    let elements = document.querySelectorAll('[data-id="' + titleId + '"]');

    $.ajax({
        url: `/watchlist/add/ + ${titleId}`,
        type: 'POST',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data){
            console.log("added to watchlist")

            elements.forEach(elementToChange => {
                let path = elementToChange.querySelector('path');
                path.setAttribute('d', 'M9 16.2l-3.5-3.5a.984.984 0 0 0-1.4 0 .984.984 0 0 0 0 1.4l4.19 4.19c.39.39 1.02.39 1.41 0L20.3 7.7a.984.984 0 0 0 0-1.4.984.984 0 0 0-1.4 0L9 16.2z')

                elementToChange.removeAttribute('onclick');
                elementToChange.setAttribute('onclick', 'removeFromWatchlist(this)')

                if (elementToChange.querySelector('span').textContent === 'In Watchlist'){
                    elementToChange.querySelector('span').textContent = "Add to Watchlist"
                }
            })


        }
    });
}

async function removeFromWatchlist(element){
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let titleId = element.dataset.id

    let elements = document.querySelectorAll('[data-id="' + titleId + '"]');


    $.ajax({
        url: `/watchlist/remove/ + ${titleId}`,
        type: 'POST',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data){
            console.log("removed from watchlist")

            elements.forEach(elementToChange => {
                let path = elementToChange.querySelector('path');
                path.setAttribute('d', 'M18 13h-5v5c0 .55-.45 1-1 1s-1-.45-1-1v-5H6c-.55 0-1-.45-1-1s.45-1 1-1h5V6c0-.55.45-1 1-1s1 .45 1 1v5h5c.55 0 1 .45 1 1s-.45 1-1 1z')

                elementToChange.removeAttribute('onclick');
                elementToChange.setAttribute('onclick', 'addToWatchlist(this)')
            })


        }
    });
}


async function addToWatchlistTitlePage(element){

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let titleId = element.dataset.id

    let elements = document.querySelectorAll('[data-id="' + titleId + '"]');

    $.ajax({
        url: `/watchlist/add/ + ${titleId}`,
        type: 'POST',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data){
            console.log("added to watchlist")

            elements.forEach(elementToChange => {
                let path = elementToChange.querySelector('path');
                path.setAttribute('d', 'M9 16.2l-3.5-3.5a.984.984 0 0 0-1.4 0 .984.984 0 0 0 0 1.4l4.19 4.19c.39.39 1.02.39 1.41 0L20.3 7.7a.984.984 0 0 0 0-1.4.984.984 0 0 0-1.4 0L9 16.2z')

                elementToChange.removeAttribute('onclick');
                elementToChange.setAttribute('onclick', 'removeFromWatchlistTitlePage(this)')
                elementToChange.querySelector('span').textContent = "In Watchlist"
            })


        }
    });
}

async function removeFromWatchlistTitlePage(element){
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let titleId = element.dataset.id

    let elements = document.querySelectorAll('[data-id="' + titleId + '"]');


    $.ajax({
        url: `/watchlist/remove/ + ${titleId}`,
        type: 'POST',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data){
            console.log("removed from watchlist")

            elements.forEach(elementToChange => {
                let path = elementToChange.querySelector('path');
                path.setAttribute('d', 'M18 13h-5v5c0 .55-.45 1-1 1s-1-.45-1-1v-5H6c-.55 0-1-.45-1-1s.45-1 1-1h5V6c0-.55.45-1 1-1s1 .45 1 1v5h5c.55 0 1 .45 1 1s-.45 1-1 1z')

                elementToChange.removeAttribute('onclick');
                elementToChange.setAttribute('onclick', 'addToWatchlistTitlePage(this)')
                elementToChange.querySelector('span').textContent = "Add to Watchlist"
            })


        }
    });
}

async function addToWatchlistWatchlistPage(element){

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let titleId = element.dataset.id

    let elements = document.querySelectorAll('[data-id="' + titleId + '"]');

    $.ajax({
        url: `/watchlist/add/ + ${titleId}`,
        type: 'POST',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data){
            console.log("added to watchlist")

            element.classList.add('d-none')

            elements.forEach(elementToChange => {
                let path = elementToChange.querySelector('path');
                path.setAttribute('d', 'M9 16.2l-3.5-3.5a.984.984 0 0 0-1.4 0 .984.984 0 0 0 0 1.4l4.19 4.19c.39.39 1.02.39 1.41 0L20.3 7.7a.984.984 0 0 0 0-1.4.984.984 0 0 0-1.4 0L9 16.2z')

                elementToChange.classList.add('d-none')
                elementToChange.removeAttribute('onclick');
                elementToChange.setAttribute('onclick', 'removeFromWatchlistTitlePage(this)')
                elementToChange.querySelector('span').textContent = "In Watchlist"
            })


        }
    });
}

async function removeFromWatchlistWatchlistPage(element){
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let titleId = element.dataset.id

    let elements = document.querySelectorAll('[data-id="' + titleId + '"]');


    $.ajax({
        url: `/watchlist/remove/ + ${titleId}`,
        type: 'POST',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data){
            console.log("removed from watchlist")

            element.classList.add('d-none')

            elements.forEach(elementToChange => {
                let path = elementToChange.querySelector('path');
                path.setAttribute('d', 'M18 13h-5v5c0 .55-.45 1-1 1s-1-.45-1-1v-5H6c-.55 0-1-.45-1-1s.45-1 1-1h5V6c0-.55.45-1 1-1s1 .45 1 1v5h5c.55 0 1 .45 1 1s-.45 1-1 1z')

                elementToChange.classList.remove('d-none')
                elementToChange.removeAttribute('onclick');
                elementToChange.setAttribute('onclick', 'addToWatchlistTitlePage(this)')
                elementToChange.querySelector('span').textContent = "Add to Watchlist"
            })


        }
    });
}