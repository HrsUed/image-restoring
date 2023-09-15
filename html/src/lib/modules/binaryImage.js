import IsingSpin from './isingSpin'
import BinaryImageBuilder from "./binaryImageBuilder";

export default class BinaryImage {
    constructor(width = 1, height = 1, blankImage = false) {
        this._width = width
        this._height = height
        this._length = width * height
        this._blank = blankImage
        this._sites = blankImage ? [] : BinaryImageBuilder.generateTreeSites(width, height)
    }

    static buildBowImage(width = 1, height = 1) {
        const image = new BinaryImage(width, height, true)
        image.sites = BinaryImageBuilder.generateBowSites(width, height)

        return image
    }

    static buildTreeImage(width = 1, height = 1) {
        const image = new BinaryImage(width, height, true)
        image.sites = BinaryImageBuilder.generateTreeSites(width, height)

        return image
    }

    get width() {
        return this._width
    }

    get height() {
        return this._height
    }

    get length() {
        return this._length
    }

    get sites() {
        return this._sites
    }

    set sites(newSites) {
        this._sites = newSites
        this._blank = newSites.length == 0
    }

    value(index) {
        return this.sites[index].value
    }

    setValue(index, value) {
        this.sites[index] = new IsingSpin(value)
    }

    clone() {
        let image = new BinaryImage(this.width, this.height, true)

        image.sites = this.sites.map(originalSite => {
            return new IsingSpin(originalSite.value)
        })

        return image
    }

    reset() {
        this.sites = []
    }

    isBlank() {
        return this._blank
    }

    createDegradedImage(probability) {
        let image = new BinaryImage(this.width, this.height, true)

        image.sites = this.clone().sites.map(site => {
            site.degrade(probability)
            return site
        })

        return image
    }

    nearestNeighborSites(siteIndex) {
        const width = this.width
        const height = this.height

        const hasTop = Math.floor(siteIndex / height) > 0
        const hasBottom = Math.floor(siteIndex / height) < height - 1
        const hasLeft = siteIndex % width > 0
        const hasRight = siteIndex % width < width - 1

        let indices = []

        if (hasTop)
            indices.push(siteIndex - width)
        if (hasBottom)
            indices.push(siteIndex + width)
        if (hasLeft)
            indices.push(siteIndex - 1)
        if (hasRight)
            indices.push(siteIndex + 1)

        return indices.map(i => this.sites[i])
    }
}
