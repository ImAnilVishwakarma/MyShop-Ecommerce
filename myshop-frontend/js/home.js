/* js/home.js */

(function () {
    "use strict";

    /* =========================================================
       1. Mobile navigation toggle
       ========================================================= */
    const navToggle = document.querySelector(".nav-toggle");
    const nav = document.getElementById("primary-nav");

    if (navToggle && nav) {
        navToggle.addEventListener("click", () => {
            const isOpen = nav.classList.toggle("is-open");
            navToggle.setAttribute("aria-expanded", String(isOpen));
        });

        // Close the menu when a link is clicked (mobile UX)
        nav.querySelectorAll("a").forEach((link) => {
            link.addEventListener("click", () => {
                nav.classList.remove("is-open");
                navToggle.setAttribute("aria-expanded", "false");
            });
        });

        // Close on Escape
        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape" && nav.classList.contains("is-open")) {
                nav.classList.remove("is-open");
                navToggle.setAttribute("aria-expanded", "false");
                navToggle.focus();
            }
        });

        // Reset state when resizing up to desktop
        window.addEventListener("resize", () => {
            if (window.innerWidth > 760) {
                nav.classList.remove("is-open");
                navToggle.setAttribute("aria-expanded", "false");
            }
        });
    }

    /* =========================================================
       2. Cart badge — reads saved cart from localStorage
       ---------------------------------------------------------
       Expects one of these shapes in localStorage under "cart":
         [ { id, name, price, qty }, ... ]      -> sums qty
         { items: [ ... ] }                     -> sums qty
         [ 1, 2, 3 ]                            -> counts items
       Falls back to 0 if nothing is found.
       ========================================================= */
    const CART_KEY = "cart";

    function getCartCount() {
        let raw;
        try {
            raw = localStorage.getItem(CART_KEY);
        } catch (error) {
            // localStorage may be blocked (private mode / permissions)
            return 0;
        }

        if (!raw) return 0;

        let data;
        try {
            data = JSON.parse(raw);
        } catch (error) {
            return 0;
        }

        const items = Array.isArray(data) ? data : data && data.items;
        if (!Array.isArray(items)) return 0;

        return items.reduce((total, item) => {
            const qty = Number(item && item.qty);
            return total + (Number.isFinite(qty) && qty > 0 ? qty : 1);
        }, 0);
    }

    function renderCartCount() {
        const badge = document.getElementById("cart-count");
        if (!badge) return;

        const count = getCartCount();
        badge.textContent = String(count);
        badge.setAttribute(
            "aria-label",
            count === 1 ? "1 item in cart" : `${count} items in cart`
        );
        badge.hidden = count === 0;
    }

    renderCartCount();

    // Keep the badge in sync if the cart changes in another tab/page
    window.addEventListener("storage", (event) => {
        if (event.key === CART_KEY) renderCartCount();
    });

    // Re-check when returning to the page (e.g., back from cart page)
    window.addEventListener("pageshow", renderCartCount);

    /* =========================================================
       3. Smooth fade-in for category cards (progressive enhancement)
       ========================================================= */
    const cards = document.querySelectorAll(".category");

    if (cards.length && "IntersectionObserver" in window) {
        cards.forEach((card) => {
            card.style.opacity = "0";
            card.style.transform = "translateY(12px)";
        });

        const observer = new IntersectionObserver(
            (entries) => {
                entries.forEach((entry, index) => {
                    if (!entry.isIntersecting) return;

                    const card = entry.target;
                    const delay = index * 70;

                    card.style.transition =
                        "opacity 0.45s ease, transform 0.45s ease, box-shadow 0.2s ease, border-color 0.2s ease";
                    card.style.transitionDelay = `${delay}ms`;
                    card.style.opacity = "1";
                    card.style.transform = "translateY(0)";

                    observer.unobserve(card);

                    // Clear inline transform/opacity after animation so CSS hover works
                    window.setTimeout(() => {
                        card.style.opacity = "";
                        card.style.transform = "";
                        card.style.transitionDelay = "";
                    }, 700 + delay);
                });
            },
            { threshold: 0.15 }
        );

        cards.forEach((card) => observer.observe(card));
    }

    /* =========================================================
       4. Log
       ========================================================= */
    console.log("MyShop Home Page loaded successfully!");
})();