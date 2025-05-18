package com.ghadynazha.recipeapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomeController {
    @GetMapping("/")
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Recipe API Dashboard</title>
                <style>
                    body {
                        background: linear-gradient(to bottom right, #ffffff, #f2f2f2);
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        justify-content: center;
                        height: 100vh;
                        margin: 0;
                    }

                    .container {
                        text-align: center;
                        padding: 30px;
                        background-color: #ffffff;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                        border-radius: 12px;
                        max-width: 600px;
                        width: 90%;
                    }

                    h1 {
                        color: #2b9348;
                        margin-bottom: 10px;
                    }

                    p {
                        font-size: 18px;
                        color: #555;
                    }

                    .links {
                        margin-top: 30px;
                    }

                    .links a {
                        display: inline-block;
                        margin: 10px;
                        padding: 10px 20px;
                        background-color: #2b9348;
                        color: white;
                        text-decoration: none;
                        border-radius: 8px;
                        transition: background-color 0.3s ease;
                    }

                    .links a:hover {
                        background-color: #20703a;
                    }

                    footer {
                        margin-top: 40px;
                        font-size: 14px;
                        color: #aaa;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>✅ Welcome to the Recipe API</h1>
                    <p>Your secure backend for managing recipes and users.</p>

                    <div class="links">
                        <a href="/swagger-ui/index.html">📘 Swagger API Docs</a>
                        <a href="/api/recipes">🍲 Browse Recipes</a>
                        <a href="https://github.com/Ghady98/recipe-api" target="_blank">💻 GitHub Repo</a>
                    </div>

                    <footer>
                        &copy; 2025 Ghady Nazha - Recipe API v1.0
                    </footer>
                </div>
            </body>
            </html>
        """;
    }
}
