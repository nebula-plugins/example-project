# example-project
An example project on how we use the nebula plugins on a simple project

## 2. Resolution rules
### a. Substitution rules

Dependency `a` depends on `substitute:1.0.0`. A substitution rule updates to `substitute:1.0.1`

`./gradlew dependencyInsight --dependency substitute --configuration testCompile`

```
> Task :dependencyInsight
sustitutionrule:substitute:1.0.1 (substitution because version 1.0.0 was accidentally released from dev_branch)

sustitutionrule:substitute:1.0.0 -> 1.0.1
\--- sustitutionrule:a:1.0.0
     \--- testCompile
```

Direct dependency on `directsubstitute:2.0.0`. A substitution rule updates `directsubstitute:2.1.0`

`./gradlew dependencyInsight --dependency directsubstitute --configuration compile`

```
> Task :dependencyInsight
sustitutionrule:directsubstitute:2.1.0 (substitution because version 2.0.0 was accidentally released from dev_branch)

sustitutionrule:directsubstitute:2.0.0 -> 2.1.0
\--- compile
```  

Dependency `arange0` depends on `rangesubstitute:2.0.0`, `arange1` depends on `rangesubstitute:2.2.0`, A rule updates range `rangesubstitute:[2.0.0,2.5.0]` to `2.5.1`.

`./gradlew dependencyInsight --dependency rangesubstitute --configuration compile`

```
> Task :dependencyInsight
sustitutionrule:rangesubstitute:2.5.1 (substitution because range contains a bug)

sustitutionrule:rangesubstitute:2.0.0 -> 2.5.1
\--- sustitutionrule:arange0:1.0.0
     \--- compile

sustitutionrule:rangesubstitute:2.2.0 -> 2.5.1
\--- sustitutionrule:arange1:1.0.0
     \--- compile
```

Dependency `asub` depends on `sub-all:1.0.0`, A rule update it to `sub-core:1.0.0`.

`./gradlew dependencyInsight --dependency sub-all --configuration compileOnly`

```
> Task :dependencyInsight
sustitutionrule:sub-core:1.0.0 (substitution because sub-all is pure evil)

sustitutionrule:sub-all:1.0.0 -> sustitutionrule:sub-core:1.0.0
\--- sustitutionrule:asub:1.0.0
     \--- compileOnly
```

### b. Alignment rule

Dependency `b0` depends on `align0:1.0.1`, `b1` depends on `align1:1.1.0`. Alignment rule should upgrade all `align*` to `1.1.0`

`./gradlew dependencyInsight --dependency align --configuration compileClasspath`

```
> Task :dependencyInsight
alignmentrule:align0:1.1.0 (aligned to 1.1.0 by align-alignfamily)

alignmentrule:align0:1.0.1 -> 1.1.0
\--- alignmentrule:b0:1.0.0
     \--- compileClasspath

alignmentrule:align1:1.1.0
\--- alignmentrule:b1:1.0.0
     \--- compileClasspath
```

A project directly depends on `direct-align0` and `direct-align1`. Both should upgrade to `1.1.0`

`./gradlew dependencyInsight --dependency align --configuration compileClasspath`

```
alignmentrule:direct-align0:1.1.0 (aligned to 1.1.0 by align-directalign)

alignmentrule:direct-align0:1.0.1 -> 1.1.0
\--- compileClasspath

alignmentrule:direct-align1:1.1.0
\--- compileClasspath
```

Dependency `b0` depends on align0:1.0.1, `b1` depends on `align1:1.1.0`, `b2` depends on `align2:1.0.2`. The dependency `align2` is exluded from alignment. `align0` and `align1` should have the same version `1.1.0`

`./gradlew dependencyInsight --dependency align --configuration compileClasspath`

```
alignmentrule:exclude-align0:1.1.0 (aligned to 1.1.0 by align-excludealigns)

alignmentrule:exclude-align0:1.0.1 -> 1.1.0
\--- alignmentrule:exclude-b0:1.0.0
     \--- compileClasspath

alignmentrule:exclude-align1:1.1.0
\--- alignmentrule:exclude-b1:1.0.0
     \--- compileClasspath

alignmentrule:exclude-align2:1.0.2
\--- alignmentrule:exclude-b2:1.0.0
     \--- compileClasspath
```

Dependency `bforce0` depends on `alignforce0:1.0.1`, `bforce1` depends on `alignforce1:1.0.2`. We force `alignforce0` to `2.0.0`, Both should be aligned to `2.0.0`.

`./gradlew dependencyInsight --dependency align --configuration compileClasspath`

```
alignmentrule:alignforce0:2.0.0 (forced)

alignmentrule:alignforce0:1.0.1 -> 2.0.0
\--- alignmentrule:bforce0:1.0.0
     \--- compileClasspath

alignmentrule:alignforce1:2.0.0 (aligned to 2.0.0 by align-alignforce)

alignmentrule:alignforce1:1.0.2 -> 2.0.0
\--- alignmentrule:bforce1:1.0.0
     \--- compileClasspath
```

### c. Reject rule

Depend on `reject:1.+`, We have reject rule on `1.0.1`. It should pick only existing `1.0.0`

`./gradlew dependencyInsight --dependency reject --configuration runtime`

```
> Task :dependencyInsight
rejectrule:reject:1.0.0

rejectrule:reject:1.+ -> 1.0.0
\--- runtime
```

### d. Replacement rule

Direct dependency `replacementex0` depends on `replacee:1.0.0`, another direct dependency `replacementex1` depends on `replacement:2.0.0`. `replacee` should be replaced by `replacement`.

`./gradlew dependencyInsight --dependency replacee --configuration runtime`

```
> Task :dependencyInsight
replacerule:replacement:2.0.0 (replacement replacerule:replacee -> replacerule:replacement)

replacerule:replacee:1.0.0 -> replacerule:replacement:2.0.0
\--- replacerule:replacementex0:1.0.0
     \--- runtime
```

### e. Exclude rule

Direct dependency `excluderule:goodlibrary:1.0.0` has a dependency on `excluderule:badlibrary:1.0.0`. The exclude rule will prevent `excluderule:badlibrary` from being in the graph.

`./gradlew dependencyInsight --dependency badlibrary --configuration compileClasspath`

```
> Task :dependencyInsight
No dependencies matching given input were found in configuration ':compileClasspath'
```

## 3. Recommender project that uses OverrideTransitives mode

"Closer" defined dependencies in recommender override transitive dependencies versions. See `override_project_example`

`./gradlew dependencyInsight --dependency transitivedeb --configuration compile`

```
> Task :dependencyInsight
overridestrategy:transitivedeb:1.0.0 (recommend 1.0.0 via override transitive recommendation)

overridestrategy:transitivedeb:1.0.1 -> 1.0.0
\--- overridestrategy:a:1.0.0
     \--- compile
```