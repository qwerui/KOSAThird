const { defineConfig } = require('@vue/cli-service')
const host = "localhost"; //back-end host
const port = "8083"; //back-end port
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: '../resources/static', // Build Directory
  devServer: {
    hot: true,
    proxy: {
      '/api/': {
        target: `http://${host}:${port}`,
        changeOrigin: true,
      },

      '/ws/': {
        target: `ws://${host}:${port}`,
        changeOrigin: false,
        ws: true,
      }
    }
  },
  configureWebpack: {
    module: {
      rules: [
        {
          test: /\.d\.ts$/,  // TypeScript 정의 파일을 무시하도록 설정
          loader: 'ignore-loader'
        }
      ]
    }
  }
});

