# example-project
An example project on how we use the nebula plugins on a simple project

## 1. Resolution rules
### a. Substitution rules

Dependency `a` depends on `substitute:1.0.0`. A substitution rule updates to `substitute:1.0.1`

`./gradlew dependencyInsigh --dependency substitute --configuration testCompile`

```
> Task :dependencyInsight
sustitutionrule:substitute:1.0.1 (substitution because version 1.0.0 was accidentally released from dev_branch)

sustitutionrule:substitute:1.0.0 -> 1.0.1
\--- sustitutionrule:a:1.0.0
     \--- testCompile
```

Direct dependency on `directsubstitute:2.0.0`. A substitution rule updates `directsubstitute:2.1.0`

`./gradlew dependencyInsigh --dependency directsubstitute --configuration compile`

```
> Task :dependencyInsight
sustitutionrule:directsubstitute:2.1.0 (substitution because version 2.0.0 was accidentally released from dev_branch)

sustitutionrule:directsubstitute:2.0.0 -> 2.1.0
\--- compile
```  

Dependency `arange0` depends on `rangesubstitute:2.0.0`, `arange1` depends on `rangesubstitute:2.2.0`, A rule updates range `rangesubstitute:[2.0.0,2.5.0]` to `2.5.1`.

`./gradlew dependencyInsigh --dependency rangesubstitute --configuration compile`

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

`./gradlew dependencyInsigh --dependency sub-all --configuration compileOnly`

```
> Task :dependencyInsight
sustitutionrule:sub-core:1.0.0 (substitution because sub-all is pure evil)

sustitutionrule:sub-all:1.0.0 -> sustitutionrule:sub-core:1.0.0
\--- sustitutionrule:asub:1.0.0
     \--- compileOnly
```