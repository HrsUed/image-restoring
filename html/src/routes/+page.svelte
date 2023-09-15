<script>
	import Image from '../lib/components/image.svelte'
	import ParameterInput from "../lib/components/parameterInput.svelte";
	import Snow from '../lib/components/snow.svelte'
	import ThemeSwitch from "../lib/components/themeSwitch.svelte";

	import BinaryImage from '../lib/modules/binaryImage'
	import ImageRestoration from "../lib/modules/imageRestoration"

	const imageSize = {
		siteSize: 5,
		width: 30,
		height: 30,
	}

	let degradeProbability = 0.1
	let theme = null

	$: nishimoriInverseTemperature = ImageRestoration.getNishimoriInverseTemperature(degradeProbability)
	$: iterationCount

	let {
		restorationInverseTemperature,
		exchangeConstant,
		errorThreshold,
		iterationCount
	} = ImageRestoration.DefaultParameters()

	$: originalImage = buildOriginalImage()
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
		originalImage = buildOriginalImage()
	}

	function setTheme(event) {
		theme = event.detail.theme
		originalImage = buildOriginalImage()
	}

	function buildOriginalImage() {
		const isWorkInProgress = !(degradedImage == undefined || degradedImage.isBlank())

		if (isWorkInProgress) return originalImage

		if (theme) {
			return BinaryImage.buildTreeImage(imageSize.width, imageSize.height)
		} else {
			return BinaryImage.buildBowImage(imageSize.width, imageSize.height)
		}
	}
</script>

<div>
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
</div>

<div class="footer">
	<ThemeSwitch on:theme-switch={setTheme} />

	<Snow />
</div>

<style>
	.util-panels {
		display: flex;
		flex-flow: row;
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

	.footer {
		padding: 0 .5rem;
		padding-bottom: 5rem;
	}
</style>
