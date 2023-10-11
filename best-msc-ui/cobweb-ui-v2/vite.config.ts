import {resolve} from 'path'
import {loadEnv} from "vite"
import type {UserConfig, ConfigEnv} from "vite"
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import progress from 'vite-plugin-progress'
// import EslintPlugin from 'vite-plugin-eslint'
import {ViteEjsPlugin} from "vite-plugin-ejs"
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
// import DefineOptions from 'unplugin-vue-define-options/vite'
import {ElementPlusResolver} from "unplugin-vue-components/resolvers"
import {createStyleImportPlugin, ElementPlusResolve} from "vite-plugin-style-import"
import UnoCSS from 'unocss/vite'

// https://vitejs.dev/config/
let root = process.cwd();

function pathResolve(dir: string) {
    return resolve(root, '.', dir)
}

export default ({command, mode}: ConfigEnv): UserConfig => {
    let env = {} as any
    let isBuild = command === 'build';
    if (isBuild) {
        env = loadEnv(mode, root);
    } else {
        env = loadEnv(process.argv[3] === '--mode' ? process.argv[4] : process.argv[3], root);
    }
    return {
        base: env.VITE_BASE_PATH,
        plugins: [
            vue(),
            vueJsx(),
            AutoImport({
                resolvers: [ElementPlusResolver()],
            }),
            Components({
                resolvers: [ElementPlusResolver()],
            }),
            progress(),
            createStyleImportPlugin({
                resolves: [ElementPlusResolve()],
                libs: [
                    {
                        libraryName: 'element-plus',
                        esModule: true,
                        resolveStyle: (name) => {
                            if ('click-outside' === name) {
                                return ''
                            }
                            return `element-plus/es/components/${name.replace(/^el-/, '')}/style/css`
                        }
                    }
                ]
            }),
            // EslintPlugin({
            //     cache: false,
            //     // 检查的文件
            //     include: [
            //         'src/**/*.vue',
            //         'src/**/*.ts',
            //         'src/**/*.tsx',
            //     ]
            // }),
            // DefineOptions(),
            ViteEjsPlugin({
                title: env.VITE_APP_TITLE
            }),
            UnoCSS()
        ],

        css: {
            preprocessorOptions: {
                less: {
                    additionalData: '@import "./src/styles/variables.module.less";',
                    javascriptEnabled: true
                }
            }
        },

        // 起个别名，在引用资源时，可以用'@/资源路径'直接访问
        resolve: {
            extensions: ['.mjs', 'js', '.ts', '.jsx', '.tsx', '.json', '.less', '.css'],
            alias: [
                {
                    find: 'vue-i18n',
                    replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
                },
                {
                    find: /\@\//,
                    replacement: `${pathResolve('src')}/`
                }
            ]
        },

        build: {
            minify: 'terser',
            outDir: env.VITE_OUT_DIR || 'dist',
            sourcemap: env.VITE_SOURCEMAP === 'true' ? 'inline' : false,
            // brotliSize : false,
            terserOptions: {
                compress: {
                    drop_debugger: env.VITE_DROP_DEBUGGER === 'true',
                    drop_console: env.VITE_DROP_CONSOLE === 'true'
                }
            }
        },

        server: {
            host: '0.0.0.0',
            port: 5200,
            https: false,
            proxy: {
                '/api': {
                    target: 'http://localhost:8135',
                    changeOrigin: true,
                    rewrite: path => path.replace(/^\/api/, '')
                }
            },
            hmr: {
                overlay: false
            }
        },

        optimizeDeps: {
            include: [
                'vue',
                'vue-router',
                'vue-types',
                'element-plus/es/locale/lang/zh-cn',
                'element-plus/es/locale/lang/en',
                '@vueuse/core',
                'axios',
                '@wangeditor/editor',
                '@wangeditor/editor-for-vue'
            ]
        }

    }
}
