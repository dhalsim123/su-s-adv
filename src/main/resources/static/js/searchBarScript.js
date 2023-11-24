    function updateSearchBar() {

        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");

        let search = document.getElementById('search').value;
        let inputGroup = document.getElementById('input-group');
        let searchResult = document.getElementById('searchResults');

        if (search === '') {
            if (searchResult != null) {
                searchResult.remove();
            }
            return;
        }

        if (searchResult != null){
            searchResult.remove();
        }

        fetch(`/search/suggestions?search=${encodeURIComponent(search)}`, {
            headers: {
                'X-requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => response.json())
            .then(data => {
                let searchResult = document.getElementById('searchResults');

                if (searchResult != null) {
                    searchResult.remove();
                }

                let suggestion = document.createElement('div');
                suggestion.id = "searchResults";
                suggestion.className = "searchResults";

                let suggestionsList = document.createElement('ul');
                suggestionsList.id = "suggestionsList";
                suggestionsList.className = "p-0";

                suggestion.appendChild(suggestionsList);


                data.forEach(function (item) {


                    let li = document.createElement('li');
                    li.classList.add("searchItem");


                    let a = document.createElement('a');


                    //Poster
                    let divPosterContainer = document.createElement('div');
                    let poster = document.createElement('img');
                    poster.src = item.mainPosterURLPhotoUrl;
                    poster.alt = '';
                    poster.classList.add('searchPoster');
                    divPosterContainer.appendChild(poster);

                    //TitleInfo

                    let divTitleInfoContainer = document.createElement('div');
                    let titleName = document.createElement('div');
                    let titleYear = document.createElement('div');
                    titleName.textContent = item.title;
                    titleYear.textContent = item.year;
                    divTitleInfoContainer.className = "d-flex flex-column p-3 text-black text-decoration-none"
                    divTitleInfoContainer.appendChild(titleName);
                    divTitleInfoContainer.appendChild(titleYear);

                    a.className = "d-flex text-decoration-none align-items-center"

                    a.href = `/title/${item.id}`
                    a.appendChild(divPosterContainer);
                    a.appendChild(divTitleInfoContainer);


                    li.appendChild(a);
                    suggestionsList.appendChild(li);

                    inputGroup.appendChild(suggestion);
                });
            })
            .catch((error) => {
                console.log('Error: ', error)
            });
    }