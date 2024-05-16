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
          "primary": "#00afff",
          "primary-content": "#ffffff",
          "accent": "#4a00ff",
          "accent-content": "#ffffff",
        },
      },
      "dark"
    ],
  },
}