const media = {
    albumList: [],
    albumHessedList: [],
    init: function () {

        document.getElementById("title").focus();
    },
    getAlbum: function () {

        this.call({title: $("#title").val()}, '/album', 'GET', (data) => {

            this.albumList = data.albumList;

            const musicList = document.getElementById("musicList");
            musicList.innerHTML = '';

            let innerHTML = '';

            innerHTML += '<table id="musicTb">';
            innerHTML +=    '<thead>';
            innerHTML +=        '<tr>';
            innerHTML +=            '<th></th>';
            innerHTML +=            '<th>제목</th>';
            innerHTML +=            '<th>가수</th>';
            innerHTML +=            '<th>앨범명</th>';
            innerHTML +=            '<th>발매일</th>';
            innerHTML +=        '</tr>';
            innerHTML +=    '</thead>';
            innerHTML +=    '<tbody id="tbyContent">';

            data.albumList.forEach((album, index) => {

                innerHTML +=    '<tr class="musicContent scrollable" data-index="' + index + '">';
                innerHTML +=        '<td><button type="button" id="btnContentToggle" class="btn btn-outline-secondary btn-sm" onclick="media.toggleRow(this)">+</button></td>';
                innerHTML +=        '<td style="text-align: left;">' + album.title + '</td>';
                innerHTML +=        '<td>' + album.singer + '</td>';
                innerHTML +=        '<td style="text-align: left;">' + album.albumName + '</td>';
                innerHTML +=        '<td>' + album.date + '</td>';
                innerHTML +=    '</tr>';
                innerHTML +=    '<tr class="hiddenLyrics">';
                innerHTML +=        '<td colspan="6">' + album.lyrics.replaceAll("\n", "<br>") + '</td>';
                innerHTML +=    '</tr>';
            });

            innerHTML +=    '</tbody>';
            innerHTML += '</table>';

            musicList.style.display = 'block';
            musicList.insertAdjacentHTML('beforeend', innerHTML);

            const contentRows = document.querySelectorAll('#tbyContent tr');
            contentRows.forEach(row => {

                const cells = row.querySelectorAll('td:not(:first-child)');

                cells.forEach(cell => cell.addEventListener('dblclick', (e) => media.addRow(e)));
            });
        });
    },
    addRow: function (current) {

        const rowIndex = current.target.parentNode.getAttribute('data-index');
        const downloadList = document.getElementById("downloadList");
        const downloadBtn = document.getElementById("downloadBtn");

        let innerHTML = '';

        if (this.isEmpty(this.albumHessedList)) {

            this.albumList.forEach((album, index) => {

                if (rowIndex == index) {

                    innerHTML += '<table id="tbDownload">';
                    innerHTML +=    '<thead>';
                    innerHTML +=        '<tr>';
                    innerHTML +=            '<th></th>';
                    innerHTML +=            '<th>제목</th>';
                    innerHTML +=            '<th>가수</th>';
                    innerHTML +=            '<th>앨범명</th>';
                    innerHTML +=            '<th>발매일</th>';
                    innerHTML +=            '<th></th>';
                    innerHTML +=        '</tr>';
                    innerHTML +=    '</thead>';
                    innerHTML +=    '<tbody id="tbyDownload">';
                    innerHTML +=        '<tr class="musicContent scrollable" data-index="0">';
                    innerHTML +=            '<td><input type="radio" name="rboDownload" onchange="media.changeRadio(this)"></td>';
                    innerHTML +=            '<td style="text-align: left;">' + album.title + '</td>';
                    innerHTML +=            '<td>' + album.singer + '</td>';
                    innerHTML +=            '<td style="text-align: left;">' + album.albumName + '</td>';
                    innerHTML +=            '<td>' + album.date + '</td>';
                    innerHTML +=            '<td><button type="button" id="btnDownloadToggle" class="btn btn-outline-secondary btn-sm" onclick="media.delRow(this)">-</button></td>';
                    innerHTML +=        '</tr>';
                    innerHTML +=    '</tbody>';
                    innerHTML += '</table>';

                    album.dunamisYn = 'N';
                    this.albumHessedList.push(album);
                }
            });

            downloadList.style.display = 'block';
            downloadBtn.style.display = 'block';
            downloadList.insertAdjacentHTML('beforeend', innerHTML);

        } else {
            this.albumList.forEach((album, index) => {

                const lastIndex = Number(document.querySelector('#tbDownload tbody tr:last-child').getAttribute('data-index')) + 1;

                if (rowIndex == index) {

                    innerHTML += '<tr class="musicContent scrollable" data-index="' + lastIndex + '">';
                    innerHTML +=    '<td><input type="radio" name="rboDownload" onchange="media.changeRadio(this)"></td>';
                    innerHTML +=    '<td style="text-align: left;">' + album.title + '</td>';
                    innerHTML +=    '<td>' + album.singer + '</td>';
                    innerHTML +=    '<td style="text-align: left;">' + album.albumName + '</td>';
                    innerHTML +=    '<td>' + album.date + '</td>';
                    innerHTML +=    '<td><button type="button" id="btnDownloadToggle" class="btn btn-outline-secondary btn-sm" onclick="media.delRow(this)">-</button></td>';
                    innerHTML += '</tr>';

                    album.dunamisYn = 'N';
                    this.albumHessedList.push(album);
                }
            });

            const tbyDownload = document.getElementById("tbyDownload");
            tbyDownload.insertAdjacentHTML('beforeend', innerHTML);
        }

        current.target.parentNode.nextElementSibling.remove();
        current.target.parentNode.remove();
    },
    delRow: function (current) {

        const rowIndex = Number(current.parentNode.parentNode.getAttribute('data-index'));

        this.albumHessedList.forEach((album, index) => {
            if (rowIndex === index) {
                this.albumHessedList.splice(index, 1);
            }
        });

        if (this.isEmpty(this.albumHessedList)) {
            document.querySelector('#tbDownload').remove();
        } else {
            current.parentNode.parentNode.remove();

            // const downloadLastRow = document.querySelector('#tbDownload tbody tr:last-child');
            // downloadLastRow.querySelector('input[type="radio"]').checked = true;
        }
    },
    downloadAlbum: function () {

        if (this.isEmpty(this.albumHessedList)) {
            alert("등록된 리스트가 없습니다.");
            return;
        }

        this.call(JSON.stringify(this.albumHessedList), '/download', 'POST', (data) => alert("다운로드 되었습니다."));
    },
    changeRadio: function (current) {

        const radioButtons = document.querySelectorAll('input[name="rboDownload"]');
        const rowIndex = Number(current.parentNode.parentNode.getAttribute('data-index'));

        for (let i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].checked) {
                radioButtons[i].value = 'Y';
            } else {
                radioButtons[i].value = 'N';
            }
        }

        this.albumHessedList.forEach((item, index) => {
            if (rowIndex === index) {
                item.dunamisYn = 'Y';
            } else {
                item.dunamisYn = 'N';
            }
        });
    },
    toggleRow: function (current) {

        const rowIndex = current.parentNode.parentNode.rowIndex;
        const row = document.getElementsByTagName('tr')[rowIndex + 1];

        row.classList.toggle('hiddenLyrics');
    },
    enterkey: function () {

        if (window.event.keyCode == 13) {
            this.getAlbum();
        }
    },
    isEmpty: function (value) {

        if (Array.isArray(value) && value.length === 0 || Object.keys(value).length === 0) {
            return true;
        }
        return false;
    },
    call: function (data, url, type, callback) {

        $.ajax({
            data: data,
            contentType: 'application/json',
            url: url,
            type: type,
            success: (data) => {
                if (typeof callback === 'function') {
                    callback(data);
                }
            },
            error: (error) => {
                alert("오류가 발생하였습니다.");
            }
        });
    },
    // selectRow: function (current) {
    //
    //     const rowIndex = Number(current.getAttribute('data-index'));
    //     const row = document.getElementsByClassName('musicContent')[rowIndex];
    //
    //     row.classList.add('selectedRow');
    // },
};

document.addEventListener('DOMContentLoaded', () => media.init());