import Site from './site';

export default class IsingSpin extends Site {
  private _spin: number;

  constructor(spin = IsingSpin.createRandomValue()) {
    const value = IsingSpin.measure(spin);

    super(value);

    this._spin = value;
  }

  static Up() {
    return 1;
  }
  static Down() {
    return -1;
  }

  static createRandomValue() {
    return Math.random() - 0.5;
  }

  static measure(value: number) {
    return value >= 0 ? this.Up() : this.Down();
  }

  get value() {
    return this.spin;
  }

  set value(newValue) {
    this.spin = newValue;
  }

  get spin() {
    return this._spin;
  }

  set spin(newValue) {
    this._spin = newValue;
  }

  isUp() {
    return this._spin > 0;
  }

  degrade(probability: number) {
    this.flipWithRandom(probability);
  }

  flip() {
    this._spin *= -1;
  }

  flipWithRandom(probability: number) {
    if (Math.random() < probability) {
      this.flip();
    }
  }

  measure() {
    return (this.value = IsingSpin.measure(this.value));
  }
}
