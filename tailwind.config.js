/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'deep-lilac': '#9B59B6',
        'vibrant-purple': '#8E44AD',
        'dark-purple': '#5B2C6F',
        'amethyst': '#4A235A',
        'violet-black': '#2C2C34',
        'absolute-black': '#000000',
      }
    },
  },
  plugins: [],
}