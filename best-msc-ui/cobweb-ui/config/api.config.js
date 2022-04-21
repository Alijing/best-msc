const isPro = Object.is(process.env.NODE_ENV, 'production')

console.log('--> 当前运行环境 : 【' + process.env.NODE_ENV + '】')

module.exports = {
  baseURL: isPro ? 'http://localhost:8135/' : 'http://localhost:8135/'
}
