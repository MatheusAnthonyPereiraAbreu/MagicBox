/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          light: '#9B59B6',    // Lilás Profundo
          DEFAULT: '#8E44AD',  // Roxo Vibrante
          dark: '#6D28D9',     // Roxo Escuro (purple-800)
          darker: '#4A235A',   // Ametista
        },
        secondary: {
          light: '#2C2C34',    // Preto Violeta
          DEFAULT: '#000000',  // Preto Absoluto
        },
        // Agrupamentos funcionais
        text: {
          primary: '#2C2C34',    // Para textos principais
          secondary: '#000000',   // Para textos secundários
          heading: '#9B59B6',    // Para títulos
          highlight: '#9B59B6',    // Para destaques
          dark: '#6D28D9'
        },
        background: {
          primary: '#4A235A',    // Fundo principal
          secondary: '#5B2C6F',   // Fundo secundário
          light: '#9B59B6',      // Fundo claro
          dark: '#2C2C34'        // Fundo escuro
        }
      }
    },
  },
  plugins: [],
}