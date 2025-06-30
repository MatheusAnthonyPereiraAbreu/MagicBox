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
        },
        // Paletas de cores para os cards
        card: {
          purple: {
            light: 'rgba(139, 92, 246, 0.15)',
            DEFAULT: 'rgba(139, 92, 246, 0.1)',
            dark: 'rgba(139, 92, 246, 0.3)',
            text: 'rgb(139, 92, 246)',
            border: 'rgba(139, 92, 246, 0.3)',
            hover: 'rgba(139, 92, 246, 0.5)'
          },
          blue: {
            light: 'rgba(59, 130, 246, 0.15)',
            DEFAULT: 'rgba(59, 130, 246, 0.1)',
            dark: 'rgba(59, 130, 246, 0.3)',
            text: 'rgb(59, 130, 246)',
            border: 'rgba(59, 130, 246, 0.3)',
            hover: 'rgba(59, 130, 246, 0.5)'
          },
          emerald: {
            light: 'rgba(16, 185, 129, 0.15)',
            DEFAULT: 'rgba(16, 185, 129, 0.1)',
            dark: 'rgba(16, 185, 129, 0.3)',
            text: 'rgb(16, 185, 129)',
            border: 'rgba(16, 185, 129, 0.3)',
            hover: 'rgba(16, 185, 129, 0.5)'
          },
          amber: {
            light: 'rgba(245, 158, 11, 0.15)',
            DEFAULT: 'rgba(245, 158, 11, 0.1)',
            dark: 'rgba(245, 158, 11, 0.3)',
            text: 'rgb(245, 158, 11)',
            border: 'rgba(245, 158, 11, 0.3)',
            hover: 'rgba(245, 158, 11, 0.5)'
          },
          rose: {
            light: 'rgba(244, 63, 94, 0.15)',
            DEFAULT: 'rgba(244, 63, 94, 0.1)',
            dark: 'rgba(244, 63, 94, 0.3)',
            text: 'rgb(244, 63, 94)',
            border: 'rgba(244, 63, 94, 0.3)',
            hover: 'rgba(244, 63, 94, 0.5)'
          },
          indigo: {
            light: 'rgba(99, 102, 241, 0.15)',
            DEFAULT: 'rgba(99, 102, 241, 0.1)',
            dark: 'rgba(99, 102, 241, 0.3)',
            text: 'rgb(99, 102, 241)',
            border: 'rgba(99, 102, 241, 0.3)',
            hover: 'rgba(99, 102, 241, 0.5)'
          }
        }
      }
    },
  },
  plugins: [],
}