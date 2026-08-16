package com.fitness.application.gym.exercises;

import com.fitness.application.gym.exercises.DTO.CreateExerciseDTO;
import com.fitness.application.gym.exercises.DTO.ExerciseMuscleDTO;
import com.fitness.application.gym.exercises.DTO.MuscleImpactDTO;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExerciseInitializerService {

    private final ExerciseService exerciseService;

    @Transactional
    public void seedDefaultExercises(User adminUser) {
        List<CreateExerciseDTO> defaultExercises = List.of(
            buildBenchPress(),
            buildSquat(),
            buildDeadlift(),
            buildOverheadPress(),
            buildPullUp(),
            buildBarbellRow(),

            buildInclineDumbbellPress(),
            buildDips(),
            buildRomanianDeadlift(),
            buildDumbbellLunges(),
            buildLateralRaises(),
            buildBarbellBicepCurl(),
            buildTricepsRopePushdown(),
            buildAbdominalCrunches(),
            buildStandingCalfRaises(),
            buildLatPulldown(),

            buildLegPress(),
            buildHyperextension(),
            buildHammerCurls(),
            buildLegExtension(),
            buildLyingLegCurl(),
            buildSupinatedDumbbellCurl(),
            buildUnilateralLatPulldown(),

            buildInclineDumbbellBicepCurl(),
            buildOverheadCableTricepsExtension(),
            buildLegCurlExtensionCombo()
        );

        for (CreateExerciseDTO dto : defaultExercises) {
            exerciseService.createExercise(dto, adminUser);
        }
    }

    private MuscleImpactDTO impact(Muscle muscle, Integer level) {
        return MuscleImpactDTO.builder()
                .muscle(muscle.name())
                .impactLevel(level)
                .build();
    }

    // 1. Barbell Flat Bench Press
    private CreateExerciseDTO buildBenchPress() {
        return CreateExerciseDTO.builder()
            .name("Жим штанги лежачи")
            .description("Базова вправа для грудних м'язів, передніх дельт і трицепсів. Виконується зі штангою на горизонтальній лаві.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.PECTORALIS_MAJOR_STERNOCOSTAL, 50),
                    impact(Muscle.PECTORALIS_MAJOR_CLAVICULAR, 20)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 20),
                    impact(Muscle.ANTERIOR_DELTOID, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 0),
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                ))
                .build())
            .build();
    }

    // 2. Barbell Back Squat
    private CreateExerciseDTO buildSquat() {
        return CreateExerciseDTO.builder()
            .name("Присідання зі штангою на спині")
            .description("Фундаментальна вправа для нижньої частини тіла. В першу чергу задіює квадрицепси, сідничні м'язи та м'язи-стабілізатори кора.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.QUADRICEPS, 65),
                    impact(Muscle.GLUTEUS_MAXIMUS, 25)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.HAMSTRINGS, 5),
                    impact(Muscle.ADDUCTORS, 5)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.SOLEUS, 0),
                    impact(Muscle.GASTROCNEMIUS, 0)
                ))
                .build())
            .build();
    }

    // 3. Conventional Deadlift
    private CreateExerciseDTO buildDeadlift() {
        return CreateExerciseDTO.builder()
            .name("Класична станова тяга")
            .description("Фундаментальна базова вправа, що задіює всю задню ланцюгову лінію м'язів. Розвиває силу спини, сідниць та задньої поверхні стегна.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.GLUTEUS_MAXIMUS, 35),
                    impact(Muscle.HAMSTRINGS, 30),
                    impact(Muscle.ERECTOR_SPINAE, 25)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.QUADRICEPS, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.TRAPEZIUS, 0),
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.FOREARM_FLEXORS, 0),
                    impact(Muscle.TRANSVERSUS_ABDOMINIS, 0)
                ))
                .build())
            .build();
    }

    // 4. Overhead Press (Military Press)
    private CreateExerciseDTO buildOverheadPress() {
        return CreateExerciseDTO.builder()
            .name("Армійський жим (жим стоячи)")
            .description("Ключова базова вправа для плечового пояса. Фокусується на передніх і бічних дельтах із залученням верхньої частини грудей та трицепсів.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 55),
                    impact(Muscle.LATERAL_DELTOID, 20)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 15),
                    impact(Muscle.PECTORALIS_MAJOR_CLAVICULAR, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.TRAPEZIUS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.ERECTOR_SPINAE, 0)
                ))
                .build())
            .build();
    }

    // 5. Overhand Pull-Up
    private CreateExerciseDTO buildPullUp() {
        return CreateExerciseDTO.builder()
            .name("Підтягування прямим хватом")
            .description("Вправа з власною вагою для розвитку спини. В першу чергу задіює найширші та ромбоподібні м'язи.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 65),
                    impact(Muscle.TERES_MAJOR, 15)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.BICEPS_BRACHII, 10),
                    impact(Muscle.RHOMBOIDS, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.BRACHIALIS, 0),
                    impact(Muscle.POSTERIOR_DELTOID, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                ))
                .build())
            .build();
    }

    // 6. Bent-Over Barbell Row
    private CreateExerciseDTO buildBarbellRow() {
        return CreateExerciseDTO.builder()
            .name("Тяга штанги в нахилі")
            .description("Базова вправа для товщини спини, що задіює найширші, ромбоподібні та трапецієподібні м'язи.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 50),
                    impact(Muscle.RHOMBOIDS, 25)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.TRAPEZIUS, 15),
                    impact(Muscle.POSTERIOR_DELTOID, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 0),
                    impact(Muscle.BICEPS_BRACHII, 0),
                    impact(Muscle.HAMSTRINGS, 0)
                ))
                .build())
            .build();
    }

    // 7. Incline Dumbbell Press
    private CreateExerciseDTO buildInclineDumbbellPress() {
        return CreateExerciseDTO.builder()
            .name("Жим гантелей на похилій лаві")
            .description("Базова вправа для верхньої частини грудей і плечей із більшою амплітудою руху, ніж зі штангою.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.PECTORALIS_MAJOR_CLAVICULAR, 50),
                    impact(Muscle.ANTERIOR_DELTOID, 25)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 15),
                    impact(Muscle.PECTORALIS_MAJOR_STERNOCOSTAL, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                ))
                .build())
            .build();
    }

    // 8. Dips
    private CreateExerciseDTO buildDips() {
        return CreateExerciseDTO.builder()
            .name("Віджимання на брусах")
            .description("Ефективна вправа з власною вагою для нарощування маси нижньої частини грудей, передніх дельт і сили трицепсів.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.PECTORALIS_MAJOR_STERNOCOSTAL, 45),
                    impact(Muscle.TRICEPS_BRACHII, 35)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 20)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.RHOMBOIDS, 0)
                ))
                .build())
            .build();
    }

    // 9. Romanian Deadlift
    private CreateExerciseDTO buildRomanianDeadlift() {
        return CreateExerciseDTO.builder()
            .name("Румунська станова тяга")
            .description("Тазодомінантний рух з акцентом на ексцентричну фазу для опрацювання біцепсів стегна та сідниць.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.HAMSTRINGS, 50),
                    impact(Muscle.GLUTEUS_MAXIMUS, 35)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 15)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 0),
                    impact(Muscle.TRAPEZIUS, 0)
                ))
                .build())
            .build();
    }

    // 10. Dumbbell Lunges
    private CreateExerciseDTO buildDumbbellLunges() {
        return CreateExerciseDTO.builder()
            .name("Випади з гантелями")
            .description("Одностороння вправа для нижньої частини тіла, що покращує силу ніг, баланс та активацію сідниць.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.QUADRICEPS, 45),
                    impact(Muscle.GLUTEUS_MAXIMUS, 40)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.HAMSTRINGS, 10),
                    impact(Muscle.ADDUCTORS, 5)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.SOLEUS, 0),
                    impact(Muscle.GASTROCNEMIUS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                ))
                .build())
            .build();
    }

    // 11. Dumbbell Lateral Raises
    private CreateExerciseDTO buildLateralRaises() {
        return CreateExerciseDTO.builder()
            .name("Махи гантелями через сторони")
            .description("Ізольована вправа, необхідна для побудови ширини плечей шляхом акценту на бічну голівку дельти.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.LATERAL_DELTOID, 85)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 10),
                    impact(Muscle.TRAPEZIUS, 5)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 0)
                ))
                .build())
            .build();
    }

    // 12. Standing Barbell Bicep Curl
    private CreateExerciseDTO buildBarbellBicepCurl() {
        return CreateExerciseDTO.builder()
            .name("Згинання рук зі штангою стоячи")
            .description("Класична ізольована вправа для максимальної гіпертрофії біцепса та сили передпліч.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.BICEPS_BRACHII, 80)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.BRACHIALIS, 20)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 0),
                    impact(Muscle.FOREARM_FLEXORS, 0)
                ))
                .build())
            .build();
    }

    // 13. Triceps Cable Rope Pushdown
    private CreateExerciseDTO buildTricepsRopePushdown() {
        return CreateExerciseDTO.builder()
            .name("Розгинання рук на блоці з канатною рукояттю")
            .description("Вправа на блоці для постійної напруги трицепсів з акцентом на бічну та присередню голівки.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 90)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                ))
                .build())
            .build();
    }

    // 14. Abdominal Crunches
    private CreateExerciseDTO buildAbdominalCrunches() {
        return CreateExerciseDTO.builder()
            .name("Скручування на прес")
            .description("Ізольована вправа для кора, спрямована на прямий м'яз живота для формування рельєфу та сили преса.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 85)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.TRANSVERSUS_ABDOMINIS, 15)
                ))
                .stabilizingMuscles(Set.of())
                .build())
            .build();
    }

    // 15. Standing Calf Raises
    private CreateExerciseDTO buildStandingCalfRaises() {
        return CreateExerciseDTO.builder()
            .name("Підйоми на носки стоячи")
            .description("Цільова вправа для гомілки з акцентом на литкові та камбалоподібні м'язи.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.GASTROCNEMIUS, 60),
                    impact(Muscle.SOLEUS, 30)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.HAMSTRINGS, 10)
                ))
                .stabilizingMuscles(Set.of())
                .build())
            .build();
    }

    // 16. Lat Pulldown
    private CreateExerciseDTO buildLatPulldown() {
        return CreateExerciseDTO.builder()
            .name("Тяга верхнього блоку до грудей")
            .description("Вправа на блоці для ширини спини. Відмінна альтернатива або доповнення до підтягувань з акцентом на найширші м'язи.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 60),
                    impact(Muscle.TERES_MAJOR, 15)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.BICEPS_BRACHII, 15),
                    impact(Muscle.RHOMBOIDS, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.BRACHIALIS, 0),
                    impact(Muscle.POSTERIOR_DELTOID, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                ))
                .build())
            .build();
    }

    // 17. Leg Press (Жим ногами)
    private CreateExerciseDTO buildLegPress() {
        return CreateExerciseDTO.builder()
            .name("Жим ногами в тренажері")
            .description("Базова вправа в тренажері для нижньої частини тіла, що опрацьовує квадрицепси, сідниці та біцепси стегна зі зниженим навантаженням на хребет.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.QUADRICEPS, 60),
                    impact(Muscle.GLUTEUS_MAXIMUS, 25)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.HAMSTRINGS, 10),
                    impact(Muscle.ADDUCTORS, 5)
                ))
                .stabilizingMuscles(Set.of())
                .build())
            .build();
    }

    // 18. Hyperextension (Гіперекстензія)
    private CreateExerciseDTO buildHyperextension() {
        return CreateExerciseDTO.builder()
            .name("Гіперекстензія")
            .description("Вправа для розгинання попереку, спрямована на зміцнення м'язів-розгиначів хребта, сідниць та біцепсів стегна.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 60),
                    impact(Muscle.GLUTEUS_MAXIMUS, 25)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.HAMSTRINGS, 15)
                ))
                .stabilizingMuscles(Set.of())
                .build())
            .build();
    }

    // 19. Hammer Curls (Молотки)
    private CreateExerciseDTO buildHammerCurls() {
        return CreateExerciseDTO.builder()
            .name("Згинання рук з гантелями «Молот»")
            .description("Згинання рук нейтральним хватом для опрацювання плечового м'яза (брахіалісу), плечопроменевого м'яза та біцепса.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.BRACHIALIS, 50),
                    impact(Muscle.BICEPS_BRACHII, 35)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 15)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 0)
                ))
                .build())
            .build();
    }

    // 20. Leg Extension (Разгибание ног)
    private CreateExerciseDTO buildLegExtension() {
        return CreateExerciseDTO.builder()
            .name("Розгинання ніг у тренажері")
            .description("Ізольована вправа в тренажері, призначена для акцентованого опрацювання квадрицепсів.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.QUADRICEPS, 100)
                ))
                .secondaryMuscles(Set.of())
                .stabilizingMuscles(Set.of())
                .build())
            .build();
    }

    // 21. Lying/Seated Leg Curl (Сгибание ног)
    private CreateExerciseDTO buildLyingLegCurl() {
        return CreateExerciseDTO.builder()
            .name("Згинання ніг у тренажері")
            .description("Ізольований рух у тренажері для опрацювання задньої поверхні стегна через згинання в колінному суглобі.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.HAMSTRINGS, 85)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.GASTROCNEMIUS, 15)
                ))
                .stabilizingMuscles(Set.of())
                .build())
            .build();
    }

    // 22. Dumbbell Bicep Curls with Supination (Бицепс с супинацией)
    private CreateExerciseDTO buildSupinatedDumbbellCurl() {
        return CreateExerciseDTO.builder()
            .name("Згинання рук з гантелями з супінацією")
            .description("Вправа на біцепс із розворотом кисті назовні (супінацією) для максимального пікового скорочення м'яза.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.BICEPS_BRACHII, 85)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.BRACHIALIS, 15)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 0)
                ))
                .build())
            .build();
    }

    // 23. Single-Arm Lat Pulldown / Row
    private CreateExerciseDTO buildUnilateralLatPulldown() {
        return CreateExerciseDTO.builder()
            .name("Важільна тяга однією рукою в тренажері")
            .description("Односторонній рух з акцентом на приведення ліктя до талії для детального опрацювання нижнього відділу найширших м'язів.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 70),
                    impact(Muscle.TERES_MAJOR, 15)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.BICEPS_BRACHII, 15)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.POSTERIOR_DELTOID, 0)
                ))
                .build())
            .build();
    }

    // 24. Incline Dumbbell Bicep Curl (Згинання рук з гантелями сидячи на похилій лаві)
    private CreateExerciseDTO buildInclineDumbbellBicepCurl() {
        return CreateExerciseDTO.builder()
            .name("Згинання рук з гантелями сидячи на похилій лаві")
            .description("Ізольована вправа для біцепса, яка завдяки нахилу лави забезпечує максимальне розтягнення довгої головки біцепса в нижній точці.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.BICEPS_BRACHII, 85)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.BRACHIALIS, 15)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 0),
                    impact(Muscle.FOREARM_FLEXORS, 0)
                ))
                .build())
            .build();
    }

    // 25. Overhead Cable Triceps Extension (Розгинання рук з-за голови на верхньому/нижньому блоці стоячи спиною до тренажера)
    private CreateExerciseDTO buildOverheadCableTricepsExtension() {
        return CreateExerciseDTO.builder()
            .name("Розгинання рук з-за голови на блоці стоячи спиною до тренажера")
            .description("Ізольована вправа для трицепса з акцентом на довгу головку. Виконується спиною до кросовера з виведенням каната або рукояті з-за голови вперед і вгору.")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 90)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 10)
                ))
                .stabilizingMuscles(Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.ERECTOR_SPINAE, 0)
                ))
                .build())
            .build();
    }

    // 26. Combined Leg Curl and Extension (Згинання та розгинання ніг у тренажері)
    private CreateExerciseDTO buildLegCurlExtensionCombo() {
        return CreateExerciseDTO.builder()
            .name("Згинання та розгинання ніг у тренажері")
            .description("Комплексна або комбінована вправа для детального опрацювання квадрицепсів та передньої/задньої поверхні стегна в одному тренажері (суперсет/комбо).")
            .muscles(ExerciseMuscleDTO.builder()
                .primaryMuscles(Set.of(
                    impact(Muscle.QUADRICEPS, 50),
                    impact(Muscle.HAMSTRINGS, 50)
                ))
                .secondaryMuscles(Set.of(
                    impact(Muscle.GASTROCNEMIUS, 0)
                ))
                .stabilizingMuscles(Set.of())
                .build())
            .build();
    }
}