package com.fitness.application.gym.exercises;

import com.fitness.application.gym.exercises.dto.ExercisesDTO;
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
        List<ExercisesDTO.CreateExercise> defaultExercises = List.of(
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

        for (ExercisesDTO.CreateExercise dto : defaultExercises) {
            exerciseService.createExercise(dto, adminUser);
        }
    }

    private ExercisesDTO.MuscleImpact impact(Muscle muscle, Integer level) {
        return new ExercisesDTO.MuscleImpact(muscle.name(), null, level);
    }

    // 1. Barbell Flat Bench Press
    private ExercisesDTO.CreateExercise buildBenchPress() {
        return new ExercisesDTO.CreateExercise(
            "Жим штанги лежачи",
            "Базова вправа для грудних м'язів, передніх дельт і трицепсів. Виконується зі штангою на горизонтальній лаві.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.PECTORALIS_MAJOR_STERNOCOSTAL, 50),
                    impact(Muscle.PECTORALIS_MAJOR_CLAVICULAR, 20)
                ),
                Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 20),
                    impact(Muscle.ANTERIOR_DELTOID, 10)
                ),
                Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 0),
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                )
            )
        );
    }

    // 2. Barbell Back Squat
    private ExercisesDTO.CreateExercise buildSquat() {
        return new ExercisesDTO.CreateExercise(
            "Присідання зі штангою на спині",
            "Фундаментальна вправа для нижньої частини тіла. В першу чергу задіює квадрицепси, сідничні м'язи та м'язи-стабілізатори кора.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.QUADRICEPS, 65),
                    impact(Muscle.GLUTEUS_MAXIMUS, 25)
                ),
                Set.of(
                    impact(Muscle.HAMSTRINGS, 5),
                    impact(Muscle.ADDUCTORS, 5)
                ),
                Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.SOLEUS, 0),
                    impact(Muscle.GASTROCNEMIUS, 0)
                )
            )
        );
    }

    // 3. Conventional Deadlift
    private ExercisesDTO.CreateExercise buildDeadlift() {
        return new ExercisesDTO.CreateExercise(
            "Класична станова тяга",
            "Фундаментальна базова вправа, що задіює всю задню ланцюгову лінію м'язів. Розвиває силу спини, сідниць та задньої поверхні стегна.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.GLUTEUS_MAXIMUS, 35),
                    impact(Muscle.HAMSTRINGS, 30),
                    impact(Muscle.ERECTOR_SPINAE, 25)
                ),
                Set.of(
                    impact(Muscle.QUADRICEPS, 10)
                ),
                Set.of(
                    impact(Muscle.TRAPEZIUS, 0),
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.FOREARM_FLEXORS, 0),
                    impact(Muscle.TRANSVERSUS_ABDOMINIS, 0)
                )
            )
        );
    }

    // 4. Overhead Press (Military Press)
    private ExercisesDTO.CreateExercise buildOverheadPress() {
        return new ExercisesDTO.CreateExercise(
            "Армійський жим (жим стоячи)",
            "Ключова базова вправа для плечового пояса. Фокусується на передніх і бічних дельтах із залученням верхньої частини грудей та трицепсів.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 55),
                    impact(Muscle.LATERAL_DELTOID, 20)
                ),
                Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 15),
                    impact(Muscle.PECTORALIS_MAJOR_CLAVICULAR, 10)
                ),
                Set.of(
                    impact(Muscle.TRAPEZIUS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.ERECTOR_SPINAE, 0)
                )
            )
        );
    }

    // 5. Overhand Pull-Up
    private ExercisesDTO.CreateExercise buildPullUp() {
        return new ExercisesDTO.CreateExercise(
            "Підтягування прямим хватом",
            "Вправа з власною вагою для розвитку спини. В першу чергу задіює найширші та ромбоподібні м'язи.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 65),
                    impact(Muscle.TERES_MAJOR, 15)
                ),
                Set.of(
                    impact(Muscle.BICEPS_BRACHII, 10),
                    impact(Muscle.RHOMBOIDS, 10)
                ),
                Set.of(
                    impact(Muscle.BRACHIALIS, 0),
                    impact(Muscle.POSTERIOR_DELTOID, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                )
            )
        );
    }

    // 6. Bent-Over Barbell Row
    private ExercisesDTO.CreateExercise buildBarbellRow() {
        return new ExercisesDTO.CreateExercise(
            "Тяга штанги в нахилі",
            "Базова вправа для товщини спини, що задіює найширші, ромбоподібні та трапецієподібні м'язи.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 50),
                    impact(Muscle.RHOMBOIDS, 25)
                ),
                Set.of(
                    impact(Muscle.TRAPEZIUS, 15),
                    impact(Muscle.POSTERIOR_DELTOID, 10)
                ),
                Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 0),
                    impact(Muscle.BICEPS_BRACHII, 0),
                    impact(Muscle.HAMSTRINGS, 0)
                )
            )
        );
    }

    // 7. Incline Dumbbell Press
    private ExercisesDTO.CreateExercise buildInclineDumbbellPress() {
        return new ExercisesDTO.CreateExercise(
            "Жим гантелей на похилій лаві",
            "Базова вправа для верхньої частини грудей і плечей із більшою амплітудою руху, ніж зі штангою.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.PECTORALIS_MAJOR_CLAVICULAR, 50),
                    impact(Muscle.ANTERIOR_DELTOID, 25)
                ),
                Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 15),
                    impact(Muscle.PECTORALIS_MAJOR_STERNOCOSTAL, 10)
                ),
                Set.of(
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                )
            )
        );
    }

    // 8. Dips
    private ExercisesDTO.CreateExercise buildDips() {
        return new ExercisesDTO.CreateExercise(
            "Віджимання на брусах",
            "Ефективна вправа з власною вагою для нарощування маси нижньої частини грудей, передніх дельт і сили трицепсів.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.PECTORALIS_MAJOR_STERNOCOSTAL, 45),
                    impact(Muscle.TRICEPS_BRACHII, 35)
                ),
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 20)
                ),
                Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.RHOMBOIDS, 0)
                )
            )
        );
    }

    // 9. Romanian Deadlift
    private ExercisesDTO.CreateExercise buildRomanianDeadlift() {
        return new ExercisesDTO.CreateExercise(
            "Румунська станова тяга",
            "Тазодомінантний рух з акцентом на ексцентричну фазу для опрацювання біцепсів стегна та сідниць.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.HAMSTRINGS, 50),
                    impact(Muscle.GLUTEUS_MAXIMUS, 35)
                ),
                Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 15)
                ),
                Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 0),
                    impact(Muscle.TRAPEZIUS, 0)
                )
            )
        );
    }

    // 10. Dumbbell Lunges
    private ExercisesDTO.CreateExercise buildDumbbellLunges() {
        return new ExercisesDTO.CreateExercise(
            "Випади з гантелями",
            "Одностороння вправа для нижньої частини тіла, що покращує силу ніг, баланс та активацію сідниць.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.QUADRICEPS, 45),
                    impact(Muscle.GLUTEUS_MAXIMUS, 40)
                ),
                Set.of(
                    impact(Muscle.HAMSTRINGS, 10),
                    impact(Muscle.ADDUCTORS, 5)
                ),
                Set.of(
                    impact(Muscle.SOLEUS, 0),
                    impact(Muscle.GASTROCNEMIUS, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                )
            )
        );
    }

    // 11. Dumbbell Lateral Raises
    private ExercisesDTO.CreateExercise buildLateralRaises() {
        return new ExercisesDTO.CreateExercise(
            "Махи гантелями через сторони",
            "Ізольована вправа, необхідна для побудови ширини плечей шляхом акценту на бічну голівку дельти.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.LATERAL_DELTOID, 85)
                ),
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 10),
                    impact(Muscle.TRAPEZIUS, 5)
                ),
                Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 0)
                )
            )
        );
    }

    // 12. Standing Barbell Bicep Curl
    private ExercisesDTO.CreateExercise buildBarbellBicepCurl() {
        return new ExercisesDTO.CreateExercise(
            "Згинання рук зі штангою стоячи",
            "Класична ізольована вправа для максимальної гіпертрофії біцепса та сили передпліч.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.BICEPS_BRACHII, 80)
                ),
                Set.of(
                    impact(Muscle.BRACHIALIS, 20)
                ),
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 0),
                    impact(Muscle.FOREARM_FLEXORS, 0)
                )
            )
        );
    }

    // 13. Triceps Cable Rope Pushdown
    private ExercisesDTO.CreateExercise buildTricepsRopePushdown() {
        return new ExercisesDTO.CreateExercise(
            "Розгинання рук на блоці з канатною рукояттю",
            "Вправа на блоці для постійної напруги трицепсів з акцентом на бічну та присередню голівки.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 90)
                ),
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 10)
                ),
                Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                )
            )
        );
    }

    // 14. Abdominal Crunches
    private ExercisesDTO.CreateExercise buildAbdominalCrunches() {
        return new ExercisesDTO.CreateExercise(
            "Скручування на прес",
            "Ізольована вправа для кора, спрямована на прямий м'яз живота для формування рельєфу та сили преса.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 85)
                ),
                Set.of(
                    impact(Muscle.TRANSVERSUS_ABDOMINIS, 15)
                ),
                Set.of()
            )
        );
    }

    // 15. Standing Calf Raises
    private ExercisesDTO.CreateExercise buildStandingCalfRaises() {
        return new ExercisesDTO.CreateExercise(
            "Підйоми на носки стоячи",
            "Цільова вправа для гомілки з акцентом на литкові та камбалоподібні м'язи.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.GASTROCNEMIUS, 60),
                    impact(Muscle.SOLEUS, 30)
                ),
                Set.of(
                    impact(Muscle.HAMSTRINGS, 10)
                ),
                Set.of()
            )
        );
    }

    // 16. Lat Pulldown
    private ExercisesDTO.CreateExercise buildLatPulldown() {
        return new ExercisesDTO.CreateExercise(
            "Тяга верхнього блоку до грудей",
            "Вправа на блоці для ширини спини. Відмінна альтернатива або доповнення до підтягувань з акцентом на найширші м'язи.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 60),
                    impact(Muscle.TERES_MAJOR, 15)
                ),
                Set.of(
                    impact(Muscle.BICEPS_BRACHII, 15),
                    impact(Muscle.RHOMBOIDS, 10)
                ),
                Set.of(
                    impact(Muscle.BRACHIALIS, 0),
                    impact(Muscle.POSTERIOR_DELTOID, 0),
                    impact(Muscle.RECTUS_ABDOMINIS, 0)
                )
            )
        );
    }

    // 17. Leg Press
    private ExercisesDTO.CreateExercise buildLegPress() {
        return new ExercisesDTO.CreateExercise(
            "Жим ногами в тренажері",
            "Базова вправа в тренажері для нижньої частини тіла, що опрацьовує квадрицепси, сідниці та біцепси стегна зі зниженим навантаженням на хребет.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.QUADRICEPS, 60),
                    impact(Muscle.GLUTEUS_MAXIMUS, 25)
                ),
                Set.of(
                    impact(Muscle.HAMSTRINGS, 10),
                    impact(Muscle.ADDUCTORS, 5)
                ),
                Set.of()
            )
        );
    }

    // 18. Hyperextension
    private ExercisesDTO.CreateExercise buildHyperextension() {
        return new ExercisesDTO.CreateExercise(
            "Гіперекстензія",
            "Вправа для розгинання попереку, спрямована на зміцнення м'язів-розгиначів хребта, сідниць та біцепсів стегна.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.ERECTOR_SPINAE, 60),
                    impact(Muscle.GLUTEUS_MAXIMUS, 25)
                ),
                Set.of(
                    impact(Muscle.HAMSTRINGS, 15)
                ),
                Set.of()
            )
        );
    }

    // 19. Hammer Curls
    private ExercisesDTO.CreateExercise buildHammerCurls() {
        return new ExercisesDTO.CreateExercise(
            "Згинання рук з гантелями «Молот»",
            "Згинання рук нейтральним хватом для опрацювання плечового м'яза (брахіалісу), плечопроменевого м'яза та біцепса.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.BRACHIALIS, 50),
                    impact(Muscle.BICEPS_BRACHII, 35)
                ),
                Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 15)
                ),
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 0)
                )
            )
        );
    }

    // 20. Leg Extension
    private ExercisesDTO.CreateExercise buildLegExtension() {
        return new ExercisesDTO.CreateExercise(
            "Розгинання ніг у тренажері",
            "Ізольована вправа в тренажері, призначена для акцентованого опрацювання квадрицепсів.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.QUADRICEPS, 100)
                ),
                Set.of(),
                Set.of()
            )
        );
    }

    // 21. Lying/Seated Leg Curl
    private ExercisesDTO.CreateExercise buildLyingLegCurl() {
        return new ExercisesDTO.CreateExercise(
            "Згинання ніг у тренажері",
            "Ізольований рух у тренажері для опрацювання задньої поверхні стегна через згинання в колінному суглобі.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.HAMSTRINGS, 85)
                ),
                Set.of(
                    impact(Muscle.GASTROCNEMIUS, 15)
                ),
                Set.of()
            )
        );
    }

    // 22. Dumbbell Bicep Curls with Supination
    private ExercisesDTO.CreateExercise buildSupinatedDumbbellCurl() {
        return new ExercisesDTO.CreateExercise(
            "Згинання рук з гантелями з супінацією",
            "Вправа на біцепс із розворотом кисті назовні (супінацією) для максимального пікового скорочення м'яза.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.BICEPS_BRACHII, 85)
                ),
                Set.of(
                    impact(Muscle.BRACHIALIS, 15)
                ),
                Set.of(
                    impact(Muscle.FOREARM_FLEXORS, 0)
                )
            )
        );
    }

    // 23. Single-Arm Lat Pulldown / Row
    private ExercisesDTO.CreateExercise buildUnilateralLatPulldown() {
        return new ExercisesDTO.CreateExercise(
            "Важільна тяга однією рукою в тренажері",
            "Односторонній рух з акцентом на приведення ліктя до талії для детального опрацювання нижнього відділу найширших м'язів.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.LATISSIMUS_DORSI, 70),
                    impact(Muscle.TERES_MAJOR, 15)
                ),
                Set.of(
                    impact(Muscle.BICEPS_BRACHII, 15)
                ),
                Set.of(
                    impact(Muscle.RHOMBOIDS, 0),
                    impact(Muscle.POSTERIOR_DELTOID, 0)
                )
            )
        );
    }

    // 24. Incline Dumbbell Bicep Curl
    private ExercisesDTO.CreateExercise buildInclineDumbbellBicepCurl() {
        return new ExercisesDTO.CreateExercise(
            "Згинання рук з гантелями сидячи на похилій лаві",
            "Ізольована вправа для біцепса, яка завдяки нахилу лави забезпечує максимальне розтягнення довгої головки біцепса в нижній точці.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.BICEPS_BRACHII, 85)
                ),
                Set.of(
                    impact(Muscle.BRACHIALIS, 15)
                ),
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 0),
                    impact(Muscle.FOREARM_FLEXORS, 0)
                )
            )
        );
    }

    // 25. Overhead Cable Triceps Extension
    private ExercisesDTO.CreateExercise buildOverheadCableTricepsExtension() {
        return new ExercisesDTO.CreateExercise(
            "Розгинання рук з-за голови на блоці стоячи спиною до тренажера",
            "Ізольована вправа для трицепса з акцентом на довгу головку. Виконується спиною до кросовера з виведенням каната або рукояті з-за голови вперед і вгору.",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.TRICEPS_BRACHII, 90)
                ),
                Set.of(
                    impact(Muscle.ANTERIOR_DELTOID, 10)
                ),
                Set.of(
                    impact(Muscle.RECTUS_ABDOMINIS, 0),
                    impact(Muscle.ERECTOR_SPINAE, 0)
                )
            )
        );
    }

    // 26. Combined Leg Curl and Extension
    private ExercisesDTO.CreateExercise buildLegCurlExtensionCombo() {
        return new ExercisesDTO.CreateExercise(
            "Згинання та розгинання ніг у тренажері",
            "Комплексна або комбінована вправа для детального опрацювання квадрицепсів та передньої/задньої поверхні стегна в одному тренажері (суперсет/комбо).",
            new ExercisesDTO.ExerciseMuscle(
                Set.of(
                    impact(Muscle.QUADRICEPS, 50),
                    impact(Muscle.HAMSTRINGS, 50)
                ),
                Set.of(
                    impact(Muscle.GASTROCNEMIUS, 0)
                ),
                Set.of()
            )
        );
    }
}