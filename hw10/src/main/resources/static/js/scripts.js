function addBook() {
            const book = {
                title: document.getElementById("book-title-input").value,
                authorId: document.getElementById("book-authorId-select").value,
                genreId: document.getElementById("book-genreId-select").value
            }
            fetch("/api/v1/book/new", {
                method: 'POST',
                redirect: "follow",
                headers: {
                  'Accept': 'application/json',
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify(book)})
            .then(rawResponse => rawResponse.json())
            .then(window.location.href = "/api/v1/book")
        }