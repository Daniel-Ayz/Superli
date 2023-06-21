import pygame
from pygame.locals import *
import sys
import random
import time


# Initialize Pygame
pygame.init()

# Set up the window
window_width = 800
window_height = 600
window = pygame.display.set_mode((window_width, window_height))
pygame.display.set_caption('Snake Game')

# Set up the colors
black = (0, 0, 0)
white = (255, 255, 255)

# Set up the font
font = pygame.font.Font(None, 32)

# Function to display the main screen
def main_screen():
    username = ''
    class_name = ''
    username_input_active = False
    class_input_active = False

    cursor_blink_timer = 0
    cursor_visible = True

    while True:
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()
            elif event.type == KEYDOWN:
                if event.key == K_BACKSPACE:
                    if username_input_active and len(username) > 0:
                        username = username[:-1]
                    elif class_input_active and len(class_name) > 0:
                        class_name = class_name[:-1]
                elif event.key == K_TAB:
                    if username_input_active and len(username) > 0:
                        username += '\t'
                    elif class_input_active and len(class_name) > 0:
                        class_name += '\t'
                elif event.key == K_RETURN:
                    if len(username) > 0:
                        return username, class_name
                else:
                    if username_input_active:
                        username += event.unicode
                    elif class_input_active:
                        class_name += event.unicode
            elif event.type == MOUSEBUTTONDOWN:
                if event.button == 1:
                    username_input_active = pygame.Rect(window_width // 2 + 5, window_height // 2 - 95, 200, 30).collidepoint(event.pos)
                    class_input_active = pygame.Rect(window_width // 2 + 5, window_height // 2 + 5, 200, 30).collidepoint(event.pos)
                    start_button = pygame.Rect(window_width // 2 - 75, window_height // 2 + 150, 150, 50)
                    if start_button.collidepoint(event.pos):
                        start_game()
                else:
                    username_input_active = False
                    class_input_active = False

        draw_main_screen(username, class_name, username_input_active, class_input_active, cursor_visible)

        # Toggle cursor visibility every 0.5 seconds
        cursor_blink_timer += 1
        if cursor_blink_timer >= 700:
            cursor_visible = not cursor_visible
            cursor_blink_timer = 0

# Function to draw the main screen
def draw_main_screen(username, class_name, username_input_active, class_input_active, cursor_visible):
    window.fill(black)

    # Render the text
    username_text = font.render('Username:', True, white)
    class_text = font.render('Class:', True, white)
    username_input = font.render(username, True, white)
    class_input = font.render(class_name, True, white)
    prompt_text = font.render('Press Enter to Start', True, white)

    # Display the text on the screen
    window.blit(username_text, (window_width // 2 - 100, window_height // 2 - 100))
    window.blit(class_text, (window_width // 2 - 100, window_height // 2))
    pygame.draw.rect(window, white, (window_width // 2 + 100, window_height // 2 - 100, 200, 30), 2)
    pygame.draw.rect(window, white, (window_width // 2 + 100, window_height // 2 + 5, 200, 30), 2)
    window.blit(username_input, (window_width // 2 + 105, window_height // 2 - 95))
    window.blit(class_input, (window_width // 2 + 105, window_height // 2 + 5))
    window.blit(prompt_text, (window_width // 2 - 150, window_height // 2 + 100))

    # Draw the cursor in the active input box
    if username_input_active and cursor_visible:
        cursor_pos_x = window_width // 2 + 105 + username_input.get_width()
        pygame.draw.line(window, white, (cursor_pos_x, window_height // 2 - 95), (cursor_pos_x, window_height // 2 - 75), 2)
    elif class_input_active and cursor_visible:
        cursor_pos_x = window_width // 2 + 105 + class_input.get_width()
        pygame.draw.line(window, white, (cursor_pos_x, window_height // 2 + 5), (cursor_pos_x, window_height // 2 + 30), 2)

    # Draw the start game button
    start_button = pygame.Rect(window_width // 2 - 75, window_height // 2 + 150, 150, 50)
    pygame.draw.rect(window, white, start_button)
    start_text = font.render('Start Game', True, black)
    window.blit(start_text, (window_width // 2 - 60, window_height // 2 + 165))

    pygame.display.update()

# Function to start the game
def start_game():
    game_loop()


# Set the width and height of the game window
width, height = 640, 480
window = pygame.display.set_mode((width, height))

# Set the colors
black = pygame.Color(0, 0, 0)
white = pygame.Color(255, 255, 255)
red = pygame.Color(255, 0, 0)
blue = pygame.Color(0, 0, 255)
green = pygame.Color(0, 255, 0)
yellow = pygame.Color(255, 255, 0)
orange = pygame.Color(255, 165, 0)
head = pygame.Color(152, 238, 204)  # color for snake head

# Set the initial position of the snake
snake_pos = [100, 50]
snake_body = [[100, 50], [90, 50], [80, 50]]

# Set the initial position of the food
food_pos = [random.randrange(1, width // 10) * 10, random.randrange(1, height // 10) * 10]
food_spawn = True

# Set the initial direction of the snake
direction = 'RIGHT'
change_to = direction

# Set the game clock
clock = pygame.time.Clock()

# Set the initial score
score = 0

# Initialize the slow food position variables
slow_food_pos = [-10, -10]  # Initialize outside the visible area
fast_food_pos = [-10, -10]  # Initialize outside the visible area
bound_food_pos = [-10, -10]  # Initialize outside the visible area
slow_food_spawn = False
fast_food_spawn = False
bound_food_spawn = False
isFast = False
speed_spawn_timer = 60.0  # Time in seconds for slow food to spawn
bound_spawn_timer = 90.0  # Time in seconds for bound food to spawn
last_slow_food_spawn = time.time()
last_fast_food_spawn = time.time()
last_bound_food_spawn = time.time()
slow_timer = time.time()
fast_timer = time.time()
bound_timer = time.time()
game_start = time.time()

# Game over function
def game_over():
    font = pygame.font.SysFont('Arial', 30)
    text = font.render('Game Over!', True, red)
    game_over_rect = text.get_rect()
    game_over_rect.midtop = (width // 2, height // 4)
    window.blit(text, game_over_rect)
    pygame.display.flip()
    pygame.time.wait(2000)
    pygame.quit()
    quit()

# User screen for choosing snake color
def choose_color():
    chosen = False
    color = blue  # Default color

    color_palette = [blue, red, green, yellow]  # Add more color options here

    while not chosen:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                quit()
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_1:
                    color = color_palette[0]
                    chosen = True
                elif event.key == pygame.K_2:
                    color = color_palette[1]
                    chosen = True
                elif event.key == pygame.K_3:
                    color = color_palette[2]
                    chosen = True
                elif event.key == pygame.K_4:
                    color = color_palette[3]
                    chosen = True

        # Background
        window.fill(black)

        # Render text
        font = pygame.font.SysFont('Arial', 30)
        text = font.render('Choose Snake Color:', True, white)
        window.blit(text, (width // 2 - 150, height // 2 - 50))

        text = font.render('1. Blue', True, color_palette[0])
        window.blit(text, (width // 2 - 50, height // 2))

        text = font.render('2. Red', True, color_palette[1])
        window.blit(text, (width // 2 - 50, height // 2 + 50))

        text = font.render('3. Green', True, color_palette[2])
        window.blit(text, (width // 2 - 50, height // 2 + 100))

        text = font.render('4. Yellow', True, color_palette[3])
        window.blit(text, (width // 2 - 50, height // 2 + 150))

        pygame.display.update()

    return color

# Get the chosen snake color

def game_loop():
    snake_color = choose_color()
    # Set the width and height of the game window
    width, height = 640, 480
    window = pygame.display.set_mode((width, height))

    # Set the colors
    black = pygame.Color(0, 0, 0)
    white = pygame.Color(255, 255, 255)
    red = pygame.Color(255, 0, 0)
    blue = pygame.Color(0, 0, 255)
    green = pygame.Color(0, 255, 0)
    yellow = pygame.Color(255, 255, 0)
    orange = pygame.Color(255, 165, 0)
    head = pygame.Color(152, 238, 204)  # color for snake head

    # Set the initial position of the snake
    snake_pos = [100, 50]
    snake_body = [[100, 50], [90, 50], [80, 50]]

    # Set the initial position of the food
    food_pos = [random.randrange(1, width // 10) * 10, random.randrange(1, height // 10) * 10]
    food_spawn = True

    # Set the initial direction of the snake
    direction = 'RIGHT'
    change_to = direction

    # Set the game clock
    clock = pygame.time.Clock()

    # Set the initial score
    score = 0

    # Initialize the slow food position variables
    slow_food_pos = [-10, -10]  # Initialize outside the visible area
    fast_food_pos = [-10, -10]  # Initialize outside the visible area
    bound_food_pos = [-10, -10]  # Initialize outside the visible area
    slow_food_spawn = False
    fast_food_spawn = False
    bound_food_spawn = False
    isFast = False
    speed_spawn_timer = 60.0  # Time in seconds for slow food to spawn
    bound_spawn_timer = 90.0  # Time in seconds for bound food to spawn
    last_slow_food_spawn = time.time()
    last_fast_food_spawn = time.time()
    last_bound_food_spawn = time.time()
    slow_timer = time.time()
    fast_timer = time.time()
    bound_timer = time.time()
    game_start = time.time()












    score = 0
    # Main game loop
    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                quit()
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_RIGHT or event.key == ord('d'):
                    change_to = 'RIGHT'
                if event.key == pygame.K_LEFT or event.key == ord('a'):
                    change_to = 'LEFT'
                if event.key == pygame.K_UP or event.key == ord('w'):
                    change_to = 'UP'
                if event.key == pygame.K_DOWN or event.key == ord('s'):
                    change_to = 'DOWN'

        # Validate the direction
        if change_to == 'RIGHT' and direction != 'LEFT':
            direction = 'RIGHT'
        if change_to == 'LEFT' and direction != 'RIGHT':
            direction = 'LEFT'
        if change_to == 'UP' and direction != 'DOWN':
            direction = 'UP'
        if change_to == 'DOWN' and direction != 'UP':
            direction = 'DOWN'

        # Update the snake position
        if direction == 'RIGHT':
            snake_pos[0] = (snake_pos[0] + 10) % width
        if direction == 'LEFT':
            snake_pos[0] = (snake_pos[0] - 10 + width) % width
        if direction == 'UP':
            snake_pos[1] = (snake_pos[1] - 10 + height) % height
        if direction == 'DOWN':
            snake_pos[1] = (snake_pos[1] + 10) % height

        # Snake body mechanism
        snake_body.insert(0, list(snake_pos))
        if snake_pos[0] == food_pos[0] and snake_pos[1] == food_pos[1]:
            score += 1
            food_spawn = False
        else:
            snake_body.pop()

        # Spawn food if not present
        if not food_spawn:
            food_pos = [random.randrange(1, width // 10) * 10, random.randrange(1, height // 10) * 10]
            food_spawn = True

        # Spawn slow food every minute
        current_time = time.time()
        if slow_food_spawn and current_time - last_slow_food_spawn > 3:
            if time.time() < bound_timer:
                slow_food_pos = [(random.randrange(2, (width // 10) - 10) * 10),
                                 (random.randrange(2, (height // 10) - 10) * 10)]
            else:
                slow_food_pos = [random.randrange(0, width // 10) * 10, random.randrange(0, height // 10) * 10]
            last_slow_food_spawn = current_time
            slow_food_spawn = True

        current_time = time.time()
        if not slow_food_spawn and current_time - last_slow_food_spawn > speed_spawn_timer:
            if time.time() < bound_timer:
                slow_food_pos = [(random.randrange(2, (width // 10) - 10) * 10), (random.randrange(2, (height // 10)-10) * 10)]
            else:
                slow_food_pos = [random.randrange(0, width // 10) * 10, random.randrange(0, height // 10) * 10]
            last_slow_food_spawn = current_time
            slow_food_spawn = True

        # Spawn fast food every minute

        current_time = time.time()
        if current_time - last_fast_food_spawn > speed_spawn_timer:
            if time.time() < bound_timer:
                fast_food_pos = [(random.randrange(2, (width // 10) - 10) * 10), (random.randrange(2, (height // 10)-10) * 10)]
            else:
                fast_food_pos = [random.randrange(0, width // 10) * 10, random.randrange(0, height // 10) * 10]
            last_fast_food_spawn = current_time
            fast_food_spawn = True

        # Spawn bound food every minute
        current_time = time.time()
        if current_time - last_bound_food_spawn > bound_spawn_timer:
            bound_food_pos = [(random.randrange(2, (width // 10) - 10) * 10), (random.randrange(2, (height // 10)-10) * 10)]
            last_bound_food_spawn = current_time
            bound_food_spawn = True

        # Background
        window.fill(black)

        # Draw the snake
        for i, pos in enumerate(snake_body):
            if i == 0:
                # Draw the head of the snake in brown color
                pygame.draw.rect(window, head, pygame.Rect(pos[0], pos[1], 10, 10))
            else:
                # Draw the rest of the body in the chosen color
                pygame.draw.rect(window, snake_color, pygame.Rect(pos[0], pos[1], 10, 10))

        # Draw the regular food
        pygame.draw.rect(window, white, pygame.Rect(food_pos[0], food_pos[1], 10, 10))

        # Draw the slow food if spawned
        if slow_food_spawn:
            pygame.draw.rect(window, green, pygame.Rect(slow_food_pos[0], slow_food_pos[1], 10, 10))

        if fast_food_spawn:
            pygame.draw.rect(window,orange, pygame.Rect(fast_food_pos[0], fast_food_pos[1], 10, 10))

        if bound_food_spawn:
            pygame.draw.rect(window, red, pygame.Rect(bound_food_pos[0], bound_food_pos[1], 10, 10))

        # Check if the snake eats the slow food
        if snake_pos[0] == slow_food_pos[0] and snake_pos[1] == slow_food_pos[1]:
            score += 3
            slow_food_spawn = False
            last_slow_food_spawn = time.time()
            slow_timer = time.time() + 30  # Slow timer for 30 seconds

        # Check if the snake eats the fast food
        if snake_pos[0] == fast_food_pos[0] and snake_pos[1] == fast_food_pos[1]:
            fast_food_spawn = False
            last_fast_food_spawn = time.time()
            fast_timer = time.time() + 30  # fast timer for 30 seconds

        # Check if the snake eats the boundary food
        if snake_pos[0] == bound_food_pos[0] and snake_pos[1] == bound_food_pos[1]:
            bound_food_spawn = False
            last_bound_food_spawn = time.time()
            bound_timer = time.time() + 30  # fast timer for 30 seconds

        if time.time() < bound_timer and (snake_pos[0] == (width-10) or snake_pos[0] == 10 or snake_pos[1] == (height-10) or snake_pos[1] == 10):
            game_over()

        # Game Over conditions
        for block in snake_body[1:]:
            if snake_pos[0] == block[0] and snake_pos[1] == block[1]:
                game_over()

        # Score
        font = pygame.font.SysFont('Arial', 20)
        score_text = font.render(f'Score: {score}', True, white)
        window.blit(score_text, (10, 10))

        # Refresh the game screen
        pygame.display.update()

        if time.time() < bound_timer:
            margin_rect = pygame.Rect(0, 0, width, height)
            pygame.draw.rect(window, red, margin_rect, 10)
            pygame.display.flip()

        # Check if slow mode is active and update the clock tick rate
        if fast_timer < slow_timer:
            if time.time() < slow_timer:
                clock.tick(12)  # Slow mode tick rate of 10
            else:
                clock.tick(18)  # Regular tick rate
        else:
            if time.time() < fast_timer:
                clock.tick(40)
            else:
                clock.tick(clock.tick(18))  # Regular tick rate

game_loop()


