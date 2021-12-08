import IsingSpin from "./isingSpin";

export default class BinaryImageBuilder {
    static generateUniformSites(width, height) {
        return new Array(width * height).fill().map(_ => new IsingSpin())
    }

    static generateTreeSites(width = 30, height = 30) {
        const defaultValue = IsingSpin.Up

        const boundaryUnitHeight = height / 3
        const leftSideIndex = Math.floor((width - 1) / 2)
        const rightSideIndex = Math.ceil((width - 1) / 2)
        const gradient = 2

        const leftSideMinIndex = (length) => {
            return Math.max(leftSideIndex - length, 0)
        }

        const rightSideMaxIndex = (length) => {
            return Math.min(rightSideIndex + length, width)
        }

        let matrix = new Array(height).fill().map(_ => {
            return new Array(width).fill().map(_ => new IsingSpin(defaultValue))
        })

        // 縦に3つに分割した一番上の部分
        for (let i = 0; i < boundaryUnitHeight; i++) {
            const drawDistance = 1 + Math.floor(i / gradient)

            for (let j = leftSideIndex; j > leftSideMinIndex(drawDistance); j--) {
                matrix[i][j].flip()
            }
            for (let j = rightSideIndex; j < rightSideMaxIndex(drawDistance); j++) {
                matrix[i][j].flip()
            }
        }

        // 縦に3つに分割した真ん中の部分
        for (let i = boundaryUnitHeight; i < 2 * boundaryUnitHeight; i++) {
            const drawDistance = Math.floor(i / gradient) - 1

            for (let j = leftSideIndex; j > leftSideMinIndex(drawDistance); j--) {
                matrix[i][j].flip()
            }
            for (let j = rightSideIndex; j < rightSideMaxIndex(drawDistance); j++) {
                matrix[i][j].flip()
            }
        }

        // 縦に3つに分割した一番下の部分
        for (let i = 2 * boundaryUnitHeight; i < height; i++) {
            const drawDistance = 2

            for (let j = leftSideIndex; j > leftSideMinIndex(drawDistance); j--) {
                matrix[i][j].flip()
            }
            for (let j = rightSideIndex; j < rightSideMaxIndex(drawDistance); j++) {
                matrix[i][j].flip()
            }
        }

        return matrix.flat()
    }
}
