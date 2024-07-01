import daisyui from "daisyui"


/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{html,js,svelte,ts}'],
  theme: {
    extend: {},
  },
  plugins: [
    daisyui
  ],
  daisyui: {
    themes: [
      {
        light: {
          ...require("daisyui/src/theming/themes")["light"],
          "accent": "#4a00ff",
          "accent-content": "#ffffff",
          "--suc": "100% 0 0",
          "success-content": "#ffffff",
          "--wac": "100% 0 0",
          "warning-content": "#ffffff",
          "--erc": "100% 0 0",
          "error-content": "#ffffff",
        }
      }
    ],
  },
}