const isPro = Object.is(process.env.NODE_ENV, 'production')

console.log('--> 当前运行环境 : 【' + process.env.NODE_ENV + '】')

module.exports = {
  baseURL: isPro ? 'http://localhost:8520/msc' : 'http://localhost:8520/msc'
}
