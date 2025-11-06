// AppleStore.js
document.addEventListener("DOMContentLoaded", loadProducts);

function loadProducts() {
    fetch("http://localhost:8080/customer-server/products")
        .then(response => {
            if (!response.ok) {
                throw new Error("Ошибка загрузки данных: " + response.status);
            }
            return response.json();
        })
        .then(products => renderProducts(products))
        .catch(err => {
            console.error(err);
            alert("Помилка завантаження даних: " + err.message);
        });
}

function renderProducts(products) {
    const container = document.getElementById("products-container");

    if (!products || products.length === 0) {
        container.innerHTML = `<p style="text-align:center;">Товари відсутні</p>`;
        return;
    }

    container.innerHTML = products.map(p => `
        <div class="card">
            <img src="${p.imageUrl}" alt="${p.name}">
            <div class="card-content">
                <h3>${p.name}</h3>
                <p>${p.description}</p>
                <span class="price">${p.price} ₴</span>
            </div>
        </div>
    `).join("");
}
