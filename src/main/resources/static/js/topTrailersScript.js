function updateTopTrailer(event) {

    let element = event.currentTarget;

    let videoId = document.getElementById('trailer-top-videoId');
    let videoIdToPut = event.querySelector('p');

    console.log(videoId.textContent);
    console.log(videoIdToPut.textContent);

    if (videoId.textContent !== videoIdToPut.textContent){
        let href = document.getElementById('trailer-top-href');
        let img = document.getElementById('trailer-top-img');
        let title = document.getElementById('trailer-top-title');
        let embed = document.getElementById('trailer-top-embed');
        let div = document.getElementById('trailer-top-div');

        let newEmbed = document.createElement('iframe');
        newEmbed.id = 'trailer-top-embed';
        newEmbed.src = 'https://www.imdb.com/video/embed/' + videoIdToPut.textContent;
        newEmbed.width = '100%';
        newEmbed.height = '100%';

        embed.parentNode.replaceChild(newEmbed, embed);

        let titleToPut = event.querySelector('div').textContent
        let imageSrcToPut = event.querySelector('img').src;

        href.href = '/video/' + videoIdToPut.textContent;
        img.src = imageSrcToPut;
        title.textContent = titleToPut;
        embed.src = 'https://www.imdb.com/video/embed/' + videoIdToPut.textContent;
        videoId.textContent = videoIdToPut.textContent;


    }




}