import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [sveltekit()],
	server: {
		open: true,
		// this sets a default port to 3000, you can change this
		port: 3000,
	}
});
