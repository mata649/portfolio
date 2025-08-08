import {defineConfig} from "vite";
import {resolve} from "path";
import tailwindcss from "@tailwindcss/vite";

const build = {
    rollupOptions: {
        input: {
            main: resolve(__dirname, "index.html"),
            login: resolve(__dirname, "login/index.html"),
            admin: resolve(__dirname, "admin/index.html"),
        }
    }
};

// custom plugins
function forwardToTrailingSlashPlugin(routes) {
    return {
        name: "forward-to-trailing-slash", configureServer(server) {
            server.middlewares.use((req, _res, next) => {
                const requestURLwithoutLeadingSlash = req.url.substring(1);

                if (routes.includes(requestURLwithoutLeadingSlash)) {
                    req.url = `${req.url}/`;
                }
                next();
            });
        }
    };
}


export default defineConfig({
    appType: "mpa",
    plugins: [tailwindcss(), forwardToTrailingSlashPlugin(Object.keys(build.rollupOptions.input)),],
    build: build,

});