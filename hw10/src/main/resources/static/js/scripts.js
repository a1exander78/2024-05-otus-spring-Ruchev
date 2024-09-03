function showAllBooks() {
    fetch("/book")
        .then(response => response.json())
        .then(books => fillBookTable(books))
}

function showBook(bookId) {
    fetch("/book/" + bookId)
        .then(response => response.json())
        .then(book => outputBook(book))
}

function updateBook(bookId) {
    const book = {
        id: document.getElementById("id-input").value,
        title: document.getElementById("book-title-input").value,
        authorId: document.getElementById("book-authorId-select").value,
        genreId: document.getElementById("book-genreId-select").value
    }
    fetch("/book/" + bookId, {
        method: 'PUT',
        redirect: "follow",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)})
    .then(response => response.json())
    .then(window.location.href = "/api/v1/book")
}

function tryAddBook() {
    fetch("/book/new")
        .then(response => response.json())
        .then(options => fillOptions(options))
}

function addBook() {
    const book = {
        title: document.getElementById("book-title-input").value,
        authorId: document.getElementById("book-authorId-select").value,
        genreId: document.getElementById("book-genreId-select").value
    }
    fetch("/book/new", {
        method: 'POST',
        redirect: "follow",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)})
    .then(response => response.json())
    .then(window.location.href = "/api/v1/book")
}

function deleteBook(bookId) {
    fetch("/book/" + bookId, {
        method: 'DELETE',
        redirect: "follow",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(window.location.href = "/api/v1/book")
}

function fillBookTable(books) {
    books.forEach(book => {
        console.log(book);
        const table = document.getElementById("book-table");
        let row = table.insertRow();
        row.insertCell().innerHTML = book.id;
        row.insertCell().innerHTML = book.title;
        row.insertCell().innerHTML = book.author.fullName;
        row.insertCell().innerHTML = book.genre.name;
        row.insertCell().innerHTML = '<a href="/api/v1/comment?bookId=' + book.id + '">Show</a>';
        row.insertCell().innerHTML = '<a href="/api/v1/comment/new?bookId=' + book.id + '">Add</a>';
        row.insertCell().innerHTML = '<a href="/api/v1/book/' + book.id + '">Edit book</a>'
    })
}

function outputBook(bookWithOptions) {
    console.log(bookWithOptions);
    document.getElementById("id-input").value = bookWithOptions.book.id;
    document.getElementById("book-title-input").value = bookWithOptions.book.title;
    const authorsSelect = document.getElementById("book-authorId-select");
    const genresSelect = document.getElementById("book-genreId-select");
    fillOptions(bookWithOptions);
}

function fillOptions(options) {
    fillOptionsForSelectedAuthors(options);
    fillOptionsForSelectedGenres(options);
}

function fillOptionsForSelectedAuthors(authorOptions) {
    const authorsSelect = document.getElementById("book-authorId-select");

    for (const author of authorOptions.authors) {
        var option = new Option(author.fullName, author.id);
        if (authorOptions.book.author != null) {
            if (author.id == authorOptions.book.author.id) {
                option.setAttribute("selected", "selected");
            }
        }
        authorsSelect.add(option);
    }
}

function fillOptionsForSelectedGenres(genreOptions) {
    const genresSelect = document.getElementById("book-genreId-select");

    for (const genre of genreOptions.genres) {
        var option = new Option(genre.name, genre.id);
        if (genreOptions.book.genre != null) {
            if (genre.id == genreOptions.book.genre.id) {
                option.setAttribute("selected", "selected");
            }
        }
        genresSelect.add(option);
    }
}