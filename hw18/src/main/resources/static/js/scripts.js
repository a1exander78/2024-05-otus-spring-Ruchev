//Book AJAX scripts

function showAllBooks() {
    fetch("/api/v1/book/")
        .then(response => response.json())
        .then(books => fillBookTable(books))
}

function showBook(bookId) {
    fetch("/api/v1/book/" + bookId)
        .then(response => response.json())
        .then(book => {
            console.log(book);
            document.getElementById("book-title-input").value = book.title;
            getCatalogs(book);
        })
}

function updateBook(bookId) {
    const book = {
        id: document.getElementById("id-input").value,
        title: document.getElementById("book-title-input").value,
        authorId: document.getElementById("authorId-select").value,
        genreId: document.getElementById("genreId-select").value
    }
    fetch("/api/v1/book/" + bookId, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)})
    .then(response => response.json())
    .then(window.location.href = "/book/")
}

function addBook() {
    const book = {
        title: document.getElementById("book-title-input").value,
        authorId: document.getElementById("authorId-select").value,
        genreId: document.getElementById("genreId-select").value
    }
    fetch("/api/v1/book", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)})
    .then(response => response.json())
    .then(window.location.href = "/book/")
}

function deleteBook(bookId) {
    fetch("/api/v1/book/" + bookId, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(window.location.href = "/book/")
}

//Book utility scripts

function fillBookTable(books) {
    const table = document.getElementById("book-table");
    table.innerHTML = '';
    books.forEach(book => {
        console.log(book);
        let row = table.insertRow();
        row.insertCell().innerHTML = book.id;
        row.insertCell().innerHTML = '<a href="/book/' + book.id + '">' + book.title + '</a>';
        row.insertCell().innerHTML = book.author.fullName;
        row.insertCell().innerHTML = book.genre.name;
    })
}

//Author AJAX scripts

function showAllAuthors() {
    fetch("/api/v1/author/")
        .then(response => response.json())
        .then(authors => fillAuthorTable(authors))
}

function getAuthorsForSelect(book) {
    fetch("/api/v1/author/")
        .then(response => response.json())
        .then(authors => fillOptionsForAuthorSelect(book, authors))
}

//Author utility scripts

function fillAuthorTable(authors) {
    authors.forEach(author => {
        console.log(author);
        const table = document.getElementById("author-table");
        let row = table.insertRow();
        row.insertCell().innerHTML = author.id;
        row.insertCell().innerHTML = author.fullName;
    })
}

function fillOptionsForAuthorSelect(book, authors) {
    const authorSelect = document.getElementById("authorId-select");
    for (const author of authors) {
        console.log(author);
        var option = new Option(author.fullName, author.id);
        if (book != null) {
            if (book.author.id == author.id) {
                option.setAttribute("selected", "selected");
            }
        }
        authorSelect.add(option);
    }
}

//Genre AJAX scripts

function showAllGenres() {
    fetch("/api/v1/genre/")
        .then(response => response.json())
        .then(genres => fillGenreTable(genres))
}

function getGenresForSelect(book) {
    fetch("/api/v1/genre/")
        .then(response => response.json())
        .then(genres => fillOptionsForGenreSelect(book, genres))
}

//Genre utility scripts

function fillGenreTable(genres) {
    genres.forEach(genre => {
        console.log(genre);
        const table = document.getElementById("genre-table");
        let row = table.insertRow();
        row.insertCell().innerHTML = genre.id;
        row.insertCell().innerHTML = genre.name;
    })
}

function fillOptionsForGenreSelect(book, genres) {
    const genreSelect = document.getElementById("genreId-select");
    for (const genre of genres) {
        console.log(genre);
        var option = new Option(genre.name, genre.id);
        if (book != null) {
            if (book.genre.id == genre.id) {
                option.setAttribute("selected", "selected");
            }
        }
        genreSelect.add(option);
    }
}

//Comment AJAX scripts

function showAllCommentsByBook(bookId) {
    fetch("/api/v1/comment?bookId=" + bookId)
        .then(response => response.json())
        .then(comments => fillCommentTable(comments))
}

function showComment(commentId) {
    fetch("/api/v1/comment/" + commentId)
        .then(response => response.json())
        .then(comment => {
            console.log(comment);
            document.getElementById("comment-description-input").value = comment.description;
        })
}

function updateComment(commentId) {
    const comment = {
        id: document.getElementById("id-input").value,
        description: document.getElementById("comment-description-input").value,
    }
    fetch("/api/v1/comment/" + commentId, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(comment)})
    .then(response => response.json())
    .then(window.location.href = "/book/")
}

function addComment(bookId) {
    const comment = {
        bookId: bookId,
        description: document.getElementById("comment-description-input").value,
    }
    fetch("/api/v1/comment", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(comment)})
    .then(response => response.json())
    .then(window.location.href = "/book/")
}

function deleteComment(commentId) {
    fetch("/api/v1/comment/" + commentId, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(window.location.href = "/book/")
}

//Comment utility scripts

function fillCommentTable(comments) {
    const table = document.getElementById("comment-table");
    table.innerHTML = '';
    comments.forEach(comment => {
        console.log(comment);
        let row = table.insertRow();
        row.insertCell().innerHTML = comment.id;
        row.insertCell().innerHTML = '<a href="/comment/' + comment.id + '">' + comment.description + '</a>';
    })
}

//Common utility scripts

function getCatalogs(book) {
    getAuthorsForSelect(book);
    getGenresForSelect(book);
}