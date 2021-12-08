<script>
	import Image from './components/image.svelte'
	import ParameterInput from "./components/parameterInput.svelte";
	import Snow from './components/snow.svelte'
	import ThemeSwitch from "./components/themeSwitch.svelte";

	import BinaryImage from './modules/binaryImage'
	import ImageRestoration from "./modules/imageRestoration"

	const imageSize = {
		siteSize: 5,
		width: 30,
		height: 30,
	}

	let degradeProbability = 0.1

	$: nishimoriInverseTemperature = ImageRestoration.getNishimoriInverseTemperature(degradeProbability)
	$: iterationCount

	let {
		restorationInverseTemperature,
		exchangeConstant,
		errorThreshold,
		iterationCount
	} = ImageRestoration.DefaultParameters

	const originalImage = new BinaryImage(imageSize.width, imageSize.height)
	$: degradedImage = new BinaryImage(imageSize.width, imageSize.height, true)
	$: restoredImage = new BinaryImage(imageSize.width, imageSize.height, true)

	function degradeImage() {
		degradedImage = originalImage.createDegradedImage(degradeProbability)
	}

	function restoreImage() {
		const imageRestoration = new ImageRestoration(degradedImage)

		const restorationParameters = [
			nishimoriInverseTemperature,
			restorationInverseTemperature,
			exchangeConstant,
			errorThreshold
		]

		const result = imageRestoration.restore(...restorationParameters)
		restoredImage = result.restoredImage
		iterationCount = result.count
	}

	function resetImages() {
		iterationCount = 0
		degradedImage.reset()
		restoredImage.reset()
		degradedImage = degradedImage
		restoredImage = restoredImage
	}
</script>

<header>
	<h1>画像修復アプリケーション</h1>
</header>

<main>
	<div class="util-panels">
		<div class="panel">
			<div class="title">パラメータ</div>
			<div class="parameters">
				<ParameterInput bind:parameterValue={degradeProbability} step="0.1">劣化確率</ParameterInput>
				<ParameterInput bind:parameterValue={nishimoriInverseTemperature} step="0.01" disabled="true">西森逆温度</ParameterInput>
				<ParameterInput bind:parameterValue={restorationInverseTemperature} step="0.1">修復逆温度</ParameterInput>
				<ParameterInput bind:parameterValue={exchangeConstant} step="0.1">交換定数</ParameterInput>
				<ParameterInput bind:parameterValue={errorThreshold} step="0.0001">誤差</ParameterInput>
				<ParameterInput bind:parameterValue={iterationCount} disabled="true">反復回数</ParameterInput>
			</div>
		</div>

		<div class="panel">
			<div class="title">アクション</div>
			<div class="actions">
				<button type="button" on:click={degradeImage}>劣化させる</button>
				<button type="button" on:click={restoreImage} disabled={degradedImage.isBlank()}>修復する</button>
				<button type="button" on:click={resetImages}>画像をリセット</button>
			</div>
		</div>
	</div>

	<div class="image-panel panel">
		<div>
			原画像
			<Image image={originalImage} {...imageSize} />
		</div>
		<div>
			劣化画像
			<Image image={degradedImage} {...imageSize} />
		</div>
		<div>
			修復画像
			<Image image={restoredImage} {...imageSize} />
		</div>
	</div>
</main>

<footer>
	<ThemeSwitch />

	<Snow />
</footer>

<style>
	header {
		text-align: center;
	}

	main {
		text-align: center;
		padding: 1rem;
		display: flex;
		flex-flow: row;
		flex-wrap: wrap;
	}

	footer {
		padding: 0 .5rem;
	}

	h1 {
		text-transform: uppercase;
		font-weight: 100;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}

	.util-panels {
		display: flex;
		flex-flow: column;
	}

	.panel {
		display: flex;
		flex-flow: row;
		margin: 10px;
		padding: 1rem;
		box-shadow: 0 5px 10px 1px rgba(0, 0, 0, .14);
	}

	.title {
		margin-right: 2rem;
	}

	.parameters {
		display: flex;
		flex-flow: column;
		align-items: flex-end;
	}

	.actions {
		display: flex;
		flex-flow: column;
	}

	button {
		width: 8rem;
		margin: 5px;
	}

	.image-panel {
		flex-wrap: wrap;
		justify-content: space-between;
	}

	:global(body.silent-night) {
		color: #E6E6E6;
		background-color: #004459;
	}

	:global(body.silent-night) .panel {
		box-shadow: 0 5px 10px 1px rgba(255, 255, 255, .14);
	}
</style>
