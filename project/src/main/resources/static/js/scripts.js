//User scripts

function showAllUsers() {
    fetch("/api/v1/user/")
        .then(response => response.json())
        .then(users => fillAllUsersTable(users))
}

function fillAllUsersTable(users) {
    const table = document.getElementById("user-table");
    table.innerHTML = '';
    users.forEach(user => {
        console.log(user);
        let row = table.insertRow();
        row.insertCell().innerHTML = '<a href="/user/' + user.id + '">' + user.id + '</a>';
        row.insertCell().innerHTML = user.login;
        row.insertCell().innerHTML = user.userName;
        row.insertCell().innerHTML = '<a href="/user/district?district=' + user.address.district + '">' + user.address.district + '</a>';
        row.insertCell().innerHTML = '<a href="/user/home?street=' + user.address.street + '&streetNumber=' + user.address.streetNumber + '">' + user.address.street + ', ' + user.address.streetNumber + '</a>';
        row.insertCell().innerHTML = user.address.flatNumber;
    })
}

function showAllUsersByDistrict() {
    const searchParams = new URLSearchParams(document.location.search);
    const district = searchParams.get('district');
    fetch("/api/v1/user/district?district=" + district)
            .then(response => response.json())
            .then(users => fillAllUsersTableByHomeOrDistrict(users))
}

function showAllUsersByHome() {
    const searchParams = new URLSearchParams(document.location.search);
    const street = searchParams.get('street');
    const streetNumber = searchParams.get('streetNumber');
    fetch("/api/v1/user/home?street=" + street + "&streetNumber=" + streetNumber)
            .then(response => response.json())
            .then(users => fillAllUsersTableByHomeOrDistrict(users))
}

function fillAllUsersTableByHomeOrDistrict(users) {
    const table = document.getElementById("user-table");
    table.innerHTML = '';
    users.forEach(user => {
        console.log(user);
        let row = table.insertRow();
        row.insertCell().innerHTML = '<a href="/user/' + user.id + '">' + user.id + '</a>';
        row.insertCell().innerHTML = user.login;
        row.insertCell().innerHTML = user.userName;
        row.insertCell().innerHTML = user.address.district;
        row.insertCell().innerHTML = user.address.street + ', ' + user.address.streetNumber;
        row.insertCell().innerHTML = user.address.flatNumber;
    })
}

function showSingleUser(id) {
    fetch("/api/v1/user/" + id)
        .then(response => response.json())
        .then(user => {
            console.log(user);
            fillSingleUserTable(user);
        })
}

function fillSingleUserTable(user) {
    const table = document.getElementById("user-table");
    table.innerHTML = '';
    let row = table.insertRow();
    row.insertCell().innerHTML = user.id;
    row.insertCell().innerHTML = user.login;
    row.insertCell().innerHTML = user.userName;
    row.insertCell().innerHTML = user.address.district;
    row.insertCell().innerHTML = user.address.street + ', ' + user.address.streetNumber;
    row.insertCell().innerHTML = user.address.flatNumber;
    row.insertCell().innerHTML = '<ul>' + user.authorities.map(authority => '<li>' + authority.authority + '</li>').join('') + '</ul>';
}

function addUser() {
    const authorities = document.getElementById("authority-select").options;
    const user = {
        login: document.getElementById("user-login-input").value,
        rawPassword: document.getElementById("user-password-input").value,
        district: document.getElementById("user-district-input").value,
        street: document.getElementById("user-street-input").value,
        streetNumber: document.getElementById("user-streetNumber-input").value,
        flatNumber: document.getElementById("user-flatNumber-input").value,
        authorities: Array.from(authorities).filter(authority => authority.selected).map(authority => authority.value)
    }
    fetch("/api/v1/user/", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)})
    .then(window.location.href = "/user/")
}

function deleteUser(id) {
    fetch("/api/v1/user/" + id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(window.location.href = "/user/")
}

function getAuthorities() {
    fetch("/api/v1/authority/")
        .then(response => response.json())
        .then(authorities => {
            const authoritySelect = document.getElementById("authority-select")
            authoritySelect.innerHTML = ""
            authorities.forEach(authority => authoritySelect.add(new Option(authority.authority, authority.id)));
        })
}

function changeUserAuthority() {
    const id = document.getElementById("id-input").value;
    const authorities = document.getElementById("authority-select").options;
    const user = {
        id: id,
        authorities: Array.from(authorities).filter(authority => authority.selected).map(authority => authority.value)
    }
    fetch("/api/v1/user/" + id + "/authority/", {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)})
    .then(window.location.href = "/user/" + id)
}

function changeUserName() {
    const id = document.getElementById("id-input").value;
    const name = document.getElementById("name-input").value;

    fetch("/api/v1/user/" + id + "/name?name=" + name, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(window.location.href = "/user/" + id)
}

function changeUserPassword() {
    const id = document.getElementById("id-input").value;
    const password = document.getElementById("password-input").value;

    fetch("/api/v1/user/" + id + "/password?password=" + password, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(window.location.href = "/user/" + id)
}

//Cart scripts

function showAllCarts() {
    fetch("/api/v1/cart/")
        .then(response => response.json())
        .then(carts => fillAllCartsTable(carts))
}

function fillAllCartsTable(carts) {
    const table = document.getElementById("cart-table");
    table.innerHTML = '';
    carts.forEach(cart => {
        console.log(cart);
        let row = table.insertRow();
        row.insertCell().innerHTML = '<a href="/user/' + cart.userId + '/cart/' + cart.id + '">' + cart.id + '</a>';
        row.insertCell().innerHTML = '<a href="/cart/' + cart.id + '/status/update">' + cart.cartStatus.status + '</a>';
        row.insertCell().innerHTML = '<a href="/user/' + cart.userId + '/cart/">' + cart.userId + '</a>';
        row.insertCell().innerHTML = '<ul>' + cart.bags.map(bag => '<li>' + bag.typeOfWaste + '</li>').join('') + '</ul>';
    })
}

function showAllCartsByStatus(id) {
    fetch("/api/v1/cart/status/" + id)
        .then(response => response.json())
        .then(carts => fillAllCartsTableByStatus(carts))
}

function fillAllCartsTableByStatus(carts) {
    const table = document.getElementById("cart-table");
    table.innerHTML = '';
    carts.forEach(cart => {
        console.log(cart);
        let row = table.insertRow();
        row.insertCell().innerHTML = '<a href="/user/' + cart.user.id + '/cart/' + cart.id + '">' + cart.id + '</a>';
        row.insertCell().innerHTML = '<a href="/cart/status/' + cart.cartStatusId + '">' + cart.cartStatusId + '</a>';
        row.insertCell().innerHTML = '<a href="/user/' + cart.user.id + '">' + cart.user.id + '</a>';
        row.insertCell().innerHTML = cart.user.login;
        row.insertCell().innerHTML = cart.user.userName;
        row.insertCell().innerHTML = '<a href="/user/district?district=' + cart.user.address.district + '">' + cart.user.address.district + '</a>';
        row.insertCell().innerHTML = '<a href="/user/home?street=' + cart.user.address.street + '&streetNumber=' + cart.user.address.streetNumber + '">' + cart.user.address.street + ', ' + cart.user.address.streetNumber + '</a>';
        row.insertCell().innerHTML = cart.user.address.flatNumber;
    })
}

function showAllCartsByUser(id) {
    fetch("/api/v1/cart/user/" + id)
        .then(response => response.json())
        .then(carts => fillAllCartsTableByUser(carts))
}

function fillAllCartsTableByUser(carts) {
    const table = document.getElementById("cart-table");
    table.innerHTML = '';
    carts.forEach(cart => {
        console.log(cart);
        let row = table.insertRow();
        row.insertCell().innerHTML = '<a href="/user/' + cart.userId + '/cart/' + cart.id + '">' + cart.id + '</a>';
        row.insertCell().innerHTML = cart.cartStatus.status;
        row.insertCell().innerHTML = '<a href="/user/' + cart.userId + '">' + cart.userId + '</a>';
        row.insertCell().innerHTML = '<ul>' + cart.bags.map(bag => '<li>' + bag.typeOfWaste + '</li>').join('') + '</ul>';
    })
}

function showSingleCart(id) {
    fetch("/api/v1/cart/" + id)
        .then(response => {
        if (response.ok) {
            response.json().then(cart => {
                console.log(cart);
                fillSingleCartTable(cart);
            })
        } else {
            alert("Bad request");
            window.location.href = "/";
        }
    })
}

function fillSingleCartTable(cart) {
    const table = document.getElementById("cart-table");
    table.innerHTML = '';
    console.log(cart);
    let row = table.insertRow();
    row.insertCell().innerHTML = cart.id;
    row.insertCell().innerHTML = cart.cartStatus.status;
    row.insertCell().innerHTML = '<ul>' + cart.bags.map(bag => '<li>' + bag.typeOfWaste + '</li>').join('') + '</ul>';
    row.insertCell().innerHTML = cart.user.id;
    row.insertCell().innerHTML = cart.user.login;
    row.insertCell().innerHTML = cart.user.userName;
    row.insertCell().innerHTML = cart.user.address.district;
    row.insertCell().innerHTML = cart.user.address.streetNumber;
    row.insertCell().innerHTML = cart.user.address.flatNumber;
}

function addCart() {
    const cart = {
        userId: document.getElementById("user-id-input").value,
        bagId: document.getElementById("bag-select").value
    }
    fetch("/api/v1/cart/", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cart)})
    .then(window.location.href = "/user/"+ cart.userId +"/cart/")
}

function getBags() {
    fetch("/api/v1/bag/")
        .then(response => response.json())
        .then(bags => {
            const bagSelect = document.getElementById("bag-select")
            bagSelect.innerHTML = ""
            bags.forEach(bag => bagSelect.add(new Option(bag.typeOfWaste, bag.id)));
        })
}

function deleteCart(id) {
    const userId = window.location.pathname.split('/')[2];
    fetch("/api/v1/cart/" + id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(window.location.href = "/user/" + userId + "/cart/")
}

function getCartStatuses() {
    fetch("/api/v1/status/")
        .then(response => response.json())
        .then(statuses => {
            const statusSelect = document.getElementById("status-select")
            statusSelect.innerHTML = ""
            statuses.forEach(status => statusSelect.add(new Option(status.status, status.id)));
        })
}

function updateCartStatus() {
    const cartId = document.getElementById("id-input").value;
    const statusId = document.getElementById("status-select").value;
    fetch("/api/v1/cart/" + cartId + "/status?statusId=" + statusId, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(window.location.href = "/cart/")
}

function addBagToCart() {
    const userId = window.location.pathname.split('/')[2];
    cartId = document.getElementById("cart-id-input").value;
    bagId = document.getElementById("bag-select").value;
    fetch("/api/v1/cart/" + cartId + "/bag?bagId=" + bagId, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(window.location.href = "/user/"+ userId +"/cart/" + cartId)
}

function deleteBagFromCart() {
    const userId = window.location.pathname.split('/')[2];
    cartId = document.getElementById("cart-id-input").value;
    bagId = document.getElementById("bag-select").value;
    fetch("/api/v1/cart/" + cartId + "/bag?bagId=" + bagId, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(window.location.href = "/user/"+ userId +"/cart/" + cartId)
}