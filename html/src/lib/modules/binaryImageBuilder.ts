import IsingSpin from './isingSpin';

export default class BinaryImageBuilder {
  static generateUniformSites(width: number, height: number) {
    return Array.from({ length: width * height }, () => new IsingSpin());
  }

  static generateTreeSites(width = 30, height = 30) {
    const defaultValue = IsingSpin.Up();

    const boundaryUnitHeight = height / 3;
    const leftSideIndex = Math.floor((width - 1) / 2);
    const rightSideIndex = Math.ceil((width - 1) / 2);
    const gradient = 2;

    const leftSideMinIndex = (length: number) => {
      return Math.max(leftSideIndex - length, 0);
    };

    const rightSideMaxIndex = (length: number) => {
      return Math.min(rightSideIndex + length, width);
    };

    let matrix = Array.from({ length: height }, () => {
      return Array.from({ length: width }, () => new IsingSpin(defaultValue));
    });

    // 縦に3つに分割した一番上の部分
    for (let i = 0; i < boundaryUnitHeight; i++) {
      const drawDistance = 1 + Math.floor(i / gradient);

      for (let j = leftSideIndex; j > leftSideMinIndex(drawDistance); j--) {
        matrix[i][j].flip();
      }
      for (let j = rightSideIndex; j < rightSideMaxIndex(drawDistance); j++) {
        matrix[i][j].flip();
      }
    }

    // 縦に3つに分割した真ん中の部分
    for (let i = boundaryUnitHeight; i < 2 * boundaryUnitHeight; i++) {
      const drawDistance = Math.floor(i / gradient) - 1;

      for (let j = leftSideIndex; j > leftSideMinIndex(drawDistance); j--) {
        matrix[i][j].flip();
      }
      for (let j = rightSideIndex; j < rightSideMaxIndex(drawDistance); j++) {
        matrix[i][j].flip();
      }
    }

    // 縦に3つに分割した一番下の部分
    for (let i = 2 * boundaryUnitHeight; i < height; i++) {
      const drawDistance = 2;

      for (let j = leftSideIndex; j > leftSideMinIndex(drawDistance); j--) {
        matrix[i][j].flip();
      }
      for (let j = rightSideIndex; j < rightSideMaxIndex(drawDistance); j++) {
        matrix[i][j].flip();
      }
    }

    return matrix.flat();
  }

  static generateBowSites(width = 30, height = 30) {
    const defaultValue = IsingSpin.Up();
    const radius = width / 2 - 2;
    const origin = (width * height) / 2 - width / 2;

    let sites = Array.from({ length: width * height }, () => new IsingSpin(defaultValue));

    for (let y = 0; y < radius; y++) {
      for (let x = 0; x < radius - y; x++) {
        sites[origin + x - height * y].flip();
        sites[origin - x + height * y].flip();
      }
    }

    return sites;
  }
}
