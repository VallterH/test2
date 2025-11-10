
document.addEventListener("DOMContentLoaded", () => {

    // Знаходимо ключові елементи на сторінці
    const loginContainer = document.getElementById("login-container");
    const productsContainer = document.getElementById("products-container");
    const loginForm = document.getElementById("login-form");
    const errorMessage = document.getElementById("error-message");

    // За замовчуванням продукти приховані, а логін - видимий
    // (Це також налаштовується в CSS)

    // Додаємо обробник події "submit" (відправка) для нашої форми
    loginForm.addEventListener("submit", (event) => {
        // Забороняємо формі перезавантажувати сторінку (стандартна поведінка)
        event.preventDefault();

        // Отримуємо значення з полів вводу
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        // Очищуємо попередні помилки
        errorMessage.textContent = "";

        // Викликаємо нашу функцію завантаження продуктів,
        // але тепер передаємо в неї логін і пароль
        loadProducts(email, password);
    });

    function loadProducts(email, password) {

        // 1. Кодуємо логін і пароль в Base64
        // btoa() - це вбудована функція браузера (binary-to-ASCII)
        const credentials = btoa(`${email}:${password}`);

        // 2. Робимо fetch-запит, АЛЕ тепер додаємо заголовок (headers)
        fetch("http://localhost:8080/customer-server/products", {
            method: 'GET',
            headers: {
                // Це той самий заголовок, який очікує ваш сервлет
                'Authorization': `Basic ${credentials}`
            }
        })
            .then(response => {
                // 401 означає "Unauthorized" (невірний логін/пароль)
                if (response.status === 401) {
                    throw new Error("Невірний логін або пароль");
                }
                if (!response.ok) {
                    throw new Error("Помилка завантаження даних: " + response.status);
                }
                // Якщо все добре, повертаємо JSON
                return response.json();
            })
            .then(products => {
                // Успіх!
                // Ховаємо форму логіну
                loginContainer.style.display = "none";
                // Показуємо контейнер з продуктами
                productsContainer.style.display = "grid"; // Використовуємо 'grid' для карток

                // Рендеримо продукти
                renderProducts(products);
            })
            .catch(err => {
                // Помилка (невірний пароль або інша)
                console.error(err);
                // Замість 'alert' (який блокує), показуємо помилку в div
                errorMessage.textContent = err.message;
            });
    }

    function renderProducts(products) {
        // Знаходимо внутрішній grid-контейнер
        const container = document.getElementById("products-grid");

        if (!products || products.length === 0) {
            container.innerHTML = `<p style="text-align:center;">Товари відсутні</p>`;
            return;
        }

        // Використовуємо 'loading="lazy"' для оптимізації завантаження зображень
        container.innerHTML = products.map(p => `
            <div class="card">
                <img 
                    src="${p.imageUrl || 'https://placehold.co/300x200/eee/ccc?text=No+Image'}" 
                    alt="${p.name}" 
                    loading="lazy"
                    onerror="this.src='https://placehold.co/300x200/eee/ccc?text=Error'"
                >
                <div class="card-content">
                    <h3>${p.name || 'Назва товару'}</h3>
                    <p>${p.description || 'Опис відсутній...'}</p>
                    <span class="price">${p.price || 0} ₴</span>
                </div>
            </div>
        `).join("");
    }
});