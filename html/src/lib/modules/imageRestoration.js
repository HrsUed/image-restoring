export default class ImageRestoration {
    constructor(degradedImage) {
        this.degradedImage = degradedImage.clone()
    }

    static DefaultParameters() {
        return {
            restorationInverseTemperature: 1.0,
            exchangeConstant: 1.0,
            errorThreshold: 0.0001,
            iterationCount: 0
        }
    }

    static measureSpins(image) {
        let resultImage = image.clone()

        resultImage.sites.forEach(s => s.measure())

        return resultImage
    }

    static getNishimoriInverseTemperature(probability) {
        return 0.5 * Math.log((1.0 - probability) / probability)
    }

    restore(nishimoriInverseTemperature, restorationInverseTemperature, exchangeConstant, errorThreshold) {
        const bp = nishimoriInverseTemperature
        let bm = restorationInverseTemperature
        const iterationMaxCount = 1000

        let restoredImage = this.degradedImage.clone()
        let error = 0.0
        let iterationCount = 0
        const temperatureDecreaseRatio = 0.01

        // 誤差が上限を下回るまで反復計算
        do {
            iterationCount += 1
            error = 0.0

            // 差分を取得できるように更新前の値をバックアップしておく
            let priorIterationImage = restoredImage.clone()

            for (let i = 0; i < this.degradedImage.length; i++) {
                let nearestNeighborTotalSpins = 0.0

                for (const s of restoredImage.nearestNeighborSites(i)) {
                    nearestNeighborTotalSpins += s.value
                }

                restoredImage.setValue(i, Math.tanh(bm * exchangeConstant * nearestNeighborTotalSpins + bp * this.degradedImage.value(i)))
                error += Math.abs(restoredImage.value(i) - priorIterationImage.value(i))
            }

            error = error / this.degradedImage.length
            bm += temperatureDecreaseRatio * restorationInverseTemperature
        } while (error >= errorThreshold && iterationCount < iterationMaxCount)

        restoredImage = ImageRestoration.measureSpins(restoredImage)

        return {restoredImage: restoredImage, count: iterationCount}
    }
}
