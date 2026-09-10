package com.fitness.application.gym.workout;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.application.exceptions.GlobalExceptionHandler;
import com.fitness.application.gym.workout.DTO.ExerciseAddDTO;
import com.fitness.application.gym.workout.DTO.WorkoutDTO;
import com.fitness.application.gym.workout.DTO.WorkoutExerciseDTO;
import com.fitness.application.gym.workout.DTO.WorkoutInfo;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.security.CurrentUser;
import com.fitness.application.users.UserService;
import com.fitness.application.users.entity.User;

@ExtendWith(MockitoExtension.class)
class WorkoutControllerTest {

    @Mock
    private WorkoutService workoutService;

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkoutController workoutController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@example.com")
                .build();

        HandlerMethodArgumentResolver currentUserResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(CurrentUser.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return testUser;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(workoutController)
                .setCustomArgumentResolvers(currentUserResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void startWorkout_shouldReturnCreated() throws Exception {
        WorkoutDTO workoutDTO = WorkoutDTO.builder()
                .id(1L)
                .status(Workout.Status.IN_PROGRESS)
                .build();

        when(userService.getUserOrThrowNotFound(testUser)).thenReturn(testUser);
        when(workoutService.startWorkout(testUser)).thenReturn(workoutDTO);

        mockMvc.perform(post("/api/gym/workout/start"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(workoutService).startWorkout(testUser);
    }

    @Test
    void stopWorkout_shouldReturnOk() throws Exception {
        WorkoutDTO workoutDTO = WorkoutDTO.builder()
                .id(1L)
                .status(Workout.Status.COMPLETED)
                .build();

        when(workoutService.completeWorkout(1L, testUser)).thenReturn(workoutDTO);

        mockMvc.perform(post("/api/gym/workout/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(workoutService).completeWorkout(1L, testUser);
    }

    @Test
    void getWorkoutInfo_shouldReturnWorkoutInfo() throws Exception {
        WorkoutInfo workoutInfo = WorkoutInfo.builder()
                .id(1L)
                .build();

        when(workoutService.getWorkoutInfo(1L)).thenReturn(workoutInfo);

        mockMvc.perform(get("/api/gym/workout/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(workoutService).getWorkoutInfo(1L);
    }

    @Test
    void addExerciseToWorkout_shouldReturnCreated() throws Exception {
        ExerciseAddDTO addDTO = new ExerciseAddDTO(1);
        WorkoutExerciseDTO weDTO = WorkoutExerciseDTO.builder()
                .id(10L)
                .orderNum(1)
                .build();

        when(userService.getUserOrThrowNotFound(testUser)).thenReturn(testUser);
        when(workoutService.addExerciseToWorkout(eq(1L), eq(2L), any(ExerciseAddDTO.class), eq(testUser)))
                .thenReturn(weDTO);

        mockMvc.perform(post("/api/gym/workout/1/exercise/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.orderNum").value(1));

        verify(workoutService).addExerciseToWorkout(eq(1L), eq(2L), any(ExerciseAddDTO.class), eq(testUser));
    }

    @Test
    void deleteExerciseFromWorkout_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/gym/workout/1/exercise/2"))
                .andExpect(status().isNoContent());

        verify(workoutService).deleteExerciseFromWorkout(1L, 2L, testUser);
    }
}

