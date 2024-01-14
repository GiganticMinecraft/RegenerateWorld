## [0.2.2](https://github.com/GiganticMinecraft/RegenerateWorld/compare/v0.2.1...v0.2.2) (2024-01-14)


### Bug Fixes

* change output jar path" ([102c53a](https://github.com/GiganticMinecraft/RegenerateWorld/commit/102c53a1bd3bbb0f2aca6efecf6dbfd6383e9546))



## [0.2.1](https://github.com/GiganticMinecraft/RegenerateWorld/compare/v0.2.0...v0.2.1) (2024-01-14)


### Bug Fixes

* update Spigot and MV-Core ([0b09193](https://github.com/GiganticMinecraft/RegenerateWorld/commit/0b091935dbf90730bfbcce580e248f870c6f6c2e))



# [0.2.0](https://github.com/GiganticMinecraft/RegenerateWorld/compare/v0.1.0...v0.2.0) (2022-08-30)


### Bug Fixes

* fix enum entry class access modifier ([ac2e167](https://github.com/GiganticMinecraft/RegenerateWorld/commit/ac2e167d2116dd8ab37babf7db03ab039e0f21ad))
* remove `UseCase#(add|remove)World` and add `#setWorlds` ([844b9fc](https://github.com/GiganticMinecraft/RegenerateWorld/commit/844b9fcca6223c198bec61a0d23cfb815216539d))


### Features

* add `/rw schedule edit` ([212e4ea](https://github.com/GiganticMinecraft/RegenerateWorld/commit/212e4ea273351776e3df1659e4a27f35a8550761))
* add ParseException.MustBeIncludedIn ([50c86cb](https://github.com/GiganticMinecraft/RegenerateWorld/commit/50c86cb3cdb539cde723e99ff4597f866a8c1439))



# [0.1.0](https://github.com/GiganticMinecraft/RegenerateWorld/compare/v0.0.1...v0.1.0) (2022-08-29)


### Bug Fixes

* /regen run schedule update nextDateTime of schedule ([4cee934](https://github.com/GiganticMinecraft/RegenerateWorld/commit/4cee934f4fd9887e2d72b95f0d250d620d56d737))
* /rw regen -> /rw run ([81ed86c](https://github.com/GiganticMinecraft/RegenerateWorld/commit/81ed86c0e410195e1c9f07e1ae846d3b4300c740))
* add ChatColor ([7448376](https://github.com/GiganticMinecraft/RegenerateWorld/commit/7448376b55a09cd6011400e4769224840d2915cd))
* BranchedExecutor should have the options with PrintUsageExecutor ([7380a4d](https://github.com/GiganticMinecraft/RegenerateWorld/commit/7380a4da06821cc1f6937f1c4343e73108382a3c))
* call WorldRegenEvent when regen ([ea81016](https://github.com/GiganticMinecraft/RegenerateWorld/commit/ea81016348fc74e6c994fc186053eadca2dbfe09))
* change interval type to Long ([8f41aff](https://github.com/GiganticMinecraft/RegenerateWorld/commit/8f41aff5bafa0b8aecc2d88257367bef1caf1625))
* enum#fromString ignores string case ([1429183](https://github.com/GiganticMinecraft/RegenerateWorld/commit/1429183074c0c2a7151ba14cb501df3efd090d85))
* execute in ContextualExecutor ([d44f1f3](https://github.com/GiganticMinecraft/RegenerateWorld/commit/d44f1f3d9c88b8cd0c3a953106c9e2b16d4589db))
* fix cast type ([e88673c](https://github.com/GiganticMinecraft/RegenerateWorld/commit/e88673c0b381f69b01c46b4099d1d59f01d4157a))
* fix command call ([34ef679](https://github.com/GiganticMinecraft/RegenerateWorld/commit/34ef679ec5982e33861fff95452c9df4e97c2868))
* fix err msg ([d7308c7](https://github.com/GiganticMinecraft/RegenerateWorld/commit/d7308c7d8fea9a4a6a9cd6df9dd7b132fdb0dd1e))
* fix list index ([c380315](https://github.com/GiganticMinecraft/RegenerateWorld/commit/c380315277daa494f93b3b0e48e1860307af3675))
* fix registered command name ([c7797d6](https://github.com/GiganticMinecraft/RegenerateWorld/commit/c7797d6f1c0502953ab884cdc0a9bd8b260e263a))
* fix typo ([fd7d020](https://github.com/GiganticMinecraft/RegenerateWorld/commit/fd7d02081aa9416eaf1b6d4c6ee203eb33eef247))
* fix var name ([e83874e](https://github.com/GiganticMinecraft/RegenerateWorld/commit/e83874edcb68bd4d358be574394cc297044844c0))
* GenerationSchedule#finish always plus Interval to now ([ac7e1f2](https://github.com/GiganticMinecraft/RegenerateWorld/commit/ac7e1f2cf5d82d99a3325a63815bd36b8fee844e))
* Interval init may throw Exception ([a969dac](https://github.com/GiganticMinecraft/RegenerateWorld/commit/a969dac9b20d9cd5f502ff95fa6816705a4c2801))
* make repo impl case class ([8644aa7](https://github.com/GiganticMinecraft/RegenerateWorld/commit/8644aa77761e1c4cf31709b13d384c4a64728e4e))
* nextDateTime must be now plus Interval ([09e8337](https://github.com/GiganticMinecraft/RegenerateWorld/commit/09e8337ea008c629b9f584e4c4d01b11fce18061))
* notify schedule remove success ([2f0298a](https://github.com/GiganticMinecraft/RegenerateWorld/commit/2f0298a395a096a8f54d399a76f1085041d9ef52))
* pretty print for schedules list ([9e66dfd](https://github.com/GiganticMinecraft/RegenerateWorld/commit/9e66dfd42a5babb996e3f7d86261a89a7c0bbe23))
* read and write directly from config ([304ec91](https://github.com/GiganticMinecraft/RegenerateWorld/commit/304ec9132ab4736b1d5247a53364a71813ab16fa))
* remove duplicated deps check ([0fb374f](https://github.com/GiganticMinecraft/RegenerateWorld/commit/0fb374f32ff6164e52eb56db56ca53fd0327ced2))
* save default config on enable ([93db219](https://github.com/GiganticMinecraft/RegenerateWorld/commit/93db219e3d9e50eea166bafd1a4f90b73401355a))
* Use Unit instead of () ([15d2847](https://github.com/GiganticMinecraft/RegenerateWorld/commit/15d2847507fb840a99b12ea10104194799c61114))
* use UseCase#add ([765979f](https://github.com/GiganticMinecraft/RegenerateWorld/commit/765979f24bae0d0cabb93f3878ee03ec47f22ac1))
* use ZonedDateTime ([e2ad9ed](https://github.com/GiganticMinecraft/RegenerateWorld/commit/e2ad9edcb397f28a5f3e16ecf5d610997014c003))
* コマンドメッセージをわかりやすく ([33744bd](https://github.com/GiganticMinecraft/RegenerateWorld/commit/33744bd41b541995351b963e78025c2d04af48bd))
* 何も変更がないときに、何かしらを変更するメソッドたちがRepository#saveを呼ばないように ([ac59d0d](https://github.com/GiganticMinecraft/RegenerateWorld/commit/ac59d0d9e547999cabc0c6336f19a446d2398964))


### Features

* add /rw regen ([ecabaf1](https://github.com/GiganticMinecraft/RegenerateWorld/commit/ecabaf15a962de47ce111397ad206f200387a459))
* add /rw regen schedule ([88a248e](https://github.com/GiganticMinecraft/RegenerateWorld/commit/88a248e9c3c9e1e69a602b0f872961ce20193b43))
* add /rw schedule remove ([4c636b2](https://github.com/GiganticMinecraft/RegenerateWorld/commit/4c636b297e0abeb10f6132573375d8737f80ad44))
* add `/rw help` ([f304ea0](https://github.com/GiganticMinecraft/RegenerateWorld/commit/f304ea02944846087ea733e60b6f2ace7e5601c5))
* add an exception in CommandException ([a565fa7](https://github.com/GiganticMinecraft/RegenerateWorld/commit/a565fa729415c43462b2314dd277f6ac57e92c9c))
* add arg parsers ([ff25ef1](https://github.com/GiganticMinecraft/RegenerateWorld/commit/ff25ef14d88cadaf682d099e4186c875200543fc))
* add Clock trait ([8c93ff4](https://github.com/GiganticMinecraft/RegenerateWorld/commit/8c93ff403ec51af6414c62181df6cb3f35f5da25))
* add config.yml ([43bc097](https://github.com/GiganticMinecraft/RegenerateWorld/commit/43bc097d4be6a68293179c3efbb0ef825fca5354))
* add ContextualExecutor ([b361c2d](https://github.com/GiganticMinecraft/RegenerateWorld/commit/b361c2d1ad81f50e553c0015083b7a9f37787c68))
* add DateTimeUnit enum ([9cbaa65](https://github.com/GiganticMinecraft/RegenerateWorld/commit/9cbaa654b18ab2d1a211be8fc49689a619b41530))
* add DateTimeUnit#fromAliasString ([49a43fa](https://github.com/GiganticMinecraft/RegenerateWorld/commit/49a43fa605c8bbfb4d5010a37f5d351b7a97f901))
* add domains ([34a4fbf](https://github.com/GiganticMinecraft/RegenerateWorld/commit/34a4fbfc8d20aa599317579183843d0bc445e1ef))
* add EchoExecutor ([b681e7d](https://github.com/GiganticMinecraft/RegenerateWorld/commit/b681e7d62665501068d4ca26efd5cd6610792bb2))
* add extension fun of Result ([b4f4114](https://github.com/GiganticMinecraft/RegenerateWorld/commit/b4f41140ede5e2e404d1de7970e607cf0cab9360))
* add extension to convert Throwable to OriginalException ([456c3d9](https://github.com/GiganticMinecraft/RegenerateWorld/commit/456c3d9a5015880419f1b6a35698e55452e02415))
* add GenerationScheduleUseCase#add ([067571e](https://github.com/GiganticMinecraft/RegenerateWorld/commit/067571e62020e6481430fbe55a4f837f635e10df))
* add infra ([44afdfa](https://github.com/GiganticMinecraft/RegenerateWorld/commit/44afdfac6e17c4cc11f8b1a6e5b4d328466edaa7))
* add init and finish method for GenerationSchedule ([98f4783](https://github.com/GiganticMinecraft/RegenerateWorld/commit/98f4783d70ecfda3f84dda1557d2fff18a9649d4))
* add Interval domain ([ffdd9b4](https://github.com/GiganticMinecraft/RegenerateWorld/commit/ffdd9b4d523d82c8c57f9d95e6a66d40207ce408))
* add Listener ([5d8a84d](https://github.com/GiganticMinecraft/RegenerateWorld/commit/5d8a84df30d6cc2d37112500fbe2296422413040))
* add ListSchedules Command ([b980c68](https://github.com/GiganticMinecraft/RegenerateWorld/commit/b980c685d330954640f040faa0e278e32eea200b))
* add main class ([979d68f](https://github.com/GiganticMinecraft/RegenerateWorld/commit/979d68f7a0fff21eee2a6d9c8f2003beb4701cf4))
* add Multiverse object ([5ad50a9](https://github.com/GiganticMinecraft/RegenerateWorld/commit/5ad50a9a298db4ed3bb3a90e36364b4bdc650e55))
* add OriginalException ([10fde7c](https://github.com/GiganticMinecraft/RegenerateWorld/commit/10fde7ceb2696daea7cf2c492ae3e07f5b576097))
* add package object in ContextualExecutor and replace Either ([209064d](https://github.com/GiganticMinecraft/RegenerateWorld/commit/209064dcceda0f2f1bcae45a073659507bb4d453))
* add parseArguments fun to ContextualExecutor ([db29e54](https://github.com/GiganticMinecraft/RegenerateWorld/commit/db29e54ff39e22074a89d2138a1f9bca166c3ad1))
* add ParseException ([fbf3611](https://github.com/GiganticMinecraft/RegenerateWorld/commit/fbf3611bcdf76946f5541f71927fde900c74cbd2))
* add parser to parse uuid ([5ab20f3](https://github.com/GiganticMinecraft/RegenerateWorld/commit/5ab20f3d315aa26a10392ac3805608279919ea40))
* add plugin.yml ([5cd4b67](https://github.com/GiganticMinecraft/RegenerateWorld/commit/5cd4b679eaa76f4e9913eb06f8ad4b289d80acff))
* add PreRegenWorldEvent ([f15c7aa](https://github.com/GiganticMinecraft/RegenerateWorld/commit/f15c7aa44fa25224f2d31f7499a32a6813c5294a))
* add presenter ([3969408](https://github.com/GiganticMinecraft/RegenerateWorld/commit/3969408edf5439fbcc4f97d705c1ca7a6d11e69a))
* add PrintUsageExecutor ([2e9ec61](https://github.com/GiganticMinecraft/RegenerateWorld/commit/2e9ec6104f2339a393b53fa71052f1dcf51381bd))
* add RegenWorldEvent ([65dca1b](https://github.com/GiganticMinecraft/RegenerateWorld/commit/65dca1b4953d11f2f0361fea483885400b01db52))
* add Setting ([f248af3](https://github.com/GiganticMinecraft/RegenerateWorld/commit/f248af3b2f9605d92f83e06c4c4712b889620433))
* add some exceptions ([7d3976c](https://github.com/GiganticMinecraft/RegenerateWorld/commit/7d3976cc6cb7cc8b6d9c7e249b2e71a5ea34b8e3))
* add some exceptions ([15b8121](https://github.com/GiganticMinecraft/RegenerateWorld/commit/15b81217c72e8cd35b8c45f1d9d4bf00563a9eeb))
* add usecases ([6c2e830](https://github.com/GiganticMinecraft/RegenerateWorld/commit/6c2e830040c1818c5432536495a83c484297a982))
* add WorldRegenerator ([a8fcd77](https://github.com/GiganticMinecraft/RegenerateWorld/commit/a8fcd77ff78a075fcf11c748853fb51ff2d73508))
* check deps on enable ([61db455](https://github.com/GiganticMinecraft/RegenerateWorld/commit/61db455ddb7d726e343500d2e4620412f71bccc1))
* impl /rw schedule add ([5dcf7fe](https://github.com/GiganticMinecraft/RegenerateWorld/commit/5dcf7fee9056b622dc9af24b6014c6de98a36d7b))
* impl changeInterval ([873ef1b](https://github.com/GiganticMinecraft/RegenerateWorld/commit/873ef1b2f258eb79530176c59ee27df0115413e3))
* impl some method for Usecase ([0f3bff5](https://github.com/GiganticMinecraft/RegenerateWorld/commit/0f3bff573e3170eca78c6cb26dccd2781addb456))
* pass reloadConfig fun to impl ([1b38120](https://github.com/GiganticMinecraft/RegenerateWorld/commit/1b38120f472ff09debf254077eafbfa0f2215ee9))
* Some exceptions should have a helper var ([1cb56ea](https://github.com/GiganticMinecraft/RegenerateWorld/commit/1cb56eaabae395574eb42a309e5783ab065dccdd))
* use Interval ([0e42ed7](https://github.com/GiganticMinecraft/RegenerateWorld/commit/0e42ed77371c225954ac5c6c0ecc304e669896d7))


### Reverts

* "chore(ci): add debug steps" ([6f1afd3](https://github.com/GiganticMinecraft/RegenerateWorld/commit/6f1afd3bcf4fb34ce421af54e21d2e5408235a06))
* "ci: setup node" ([68702bc](https://github.com/GiganticMinecraft/RegenerateWorld/commit/68702bc2a96353b78ac0a7a88115ff60a8146a47))
* "ci: use conventional changelog npm package" ([d33d469](https://github.com/GiganticMinecraft/RegenerateWorld/commit/d33d46968ca5d745309e958e7cc7078e449bc1ff))
* "ci: use default preset" ([434293b](https://github.com/GiganticMinecraft/RegenerateWorld/commit/434293b66d46da678956550c234640afad6b9207))



## 0.0.1 (2022-08-21)



