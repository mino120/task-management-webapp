$(document).ready(function() {
    const boardId = $('.board-container').data('board-id');
    
    // Load tasks for each list
    $('.list').each(function() {
        const listId = $(this).data('list-id');
        loadTasks(listId);
    });
    
    // Show add list modal
    $('#addListBtn, #addListBtnInline').click(function() {
        $('#addListModal').modal('show');
    });
    
    // Save new list
    $('#saveListBtn').click(function() {
        const listName = $('#listName').val();
        if (listName) {
            const position = $('.list').length - 1; // Subtract 1 for the "Add another list" placeholder
            
            $.ajax({
                url: '/api/lists',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    name: listName,
                    boardId: boardId,
                    position: position
                }),
                success: function(list) {
                    location.reload(); // Simple reload for now
                },
                error: function(xhr) {
                    alert('Error creating list: ' + xhr.responseText);
                }
            });
            
            $('#addListModal').modal('hide');
            $('#listName').val('');
        }
    });
    
    // Show add task modal
    $(document).on('click', '.add-card-btn', function() {
        const listId = $(this).data('list-id');
        $('#taskListId').val(listId);
        $('#addTaskModal').modal('show');
    });
    
    // Save new task
    $('#saveTaskBtn').click(function() {
        const listId = $('#taskListId').val();
        const title = $('#taskTitle').val();
        const description = $('#taskDescription').val();
        const dueDate = $('#taskDueDate').val();
        
        if (title && listId) {
            const position = $(`.list-cards[data-list-id="${listId}"] .card`).length;
            
            let dueDateObj = null;
            if (dueDate) {
                dueDateObj = new Date(dueDate).toISOString();
            }
            
            $.ajax({
                url: '/api/tasks',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    title: title,
                    description: description || null,
                    listId: parseInt(listId),
                    position: position,
                    dueDate: dueDateObj
                }),
                success: function(task) {
                    addTaskToList(task);
                    $('#addTaskModal').modal('hide');
                    $('#taskTitle').val('');
                    $('#taskDescription').val('');
                    $('#taskDueDate').val('');
                },
                error: function(xhr) {
                    alert('Error creating task: ' + xhr.responseText);
                }
            });
        }
    });
    
    // Setup drag and drop functionality
    setupDragAndDrop();
});

function loadTasks(listId) {
    $.ajax({
        url: `/api/lists/${listId}/tasks`,
        method: 'GET',
        success: function(tasks) {
            // Sort tasks by position
            tasks.sort((a, b) => a.position - b.position);
            
            // Add tasks to list
            tasks.forEach(function(task) {
                addTaskToList(task);
            });
        },
        error: function(xhr) {
            console.error('Error loading tasks:', xhr.responseText);
        }
    });
}

function addTaskToList(task) {
    const dueDate = task.dueDate ? new Date(task.dueDate).toLocaleDateString() : '';
    
    const taskHtml = `
        <div class="card" data-task-id="${task.id}" data-position="${task.position}" draggable="true">
            <div class="card-title">${task.title}</div>
            ${task.description ? `<div class="card-description">${task.description}</div>` : ''}
            ${dueDate ? `<div class="due-date"><i class="far fa-clock"></i> ${dueDate}</div>` : ''}
        </div>
    `;
    
    $(`.list-cards[data-list-id="${task.listId}"]`).append(taskHtml);
}

function setupDragAndDrop() {
    $(document).on('dragstart', '.card', function(e) {
        e.originalEvent.dataTransfer.setData('text/plain', $(this).data('task-id'));
        $(this).addClass('dragging');
    });
    
    $(document).on('dragend', '.card', function() {
        $(this).removeClass('dragging');
    });
    
    $(document).on('dragover', '.list-cards', function(e) {
        e.preventDefault();
    });
    
    $(document).on('dragenter', '.list-cards', function() {
        $(this).closest('.list').addClass('drag-over');
    });
    
    $(document).on('dragleave', '.list-cards', function() {
        $(this).closest('.list').removeClass('drag-over');
    });
    
    $(document).on('drop', '.list-cards', function(e) {
        e.preventDefault();
        $(this).closest('.list').removeClass('drag-over');
        
        const taskId = e.originalEvent.dataTransfer.getData('text/plain');
        const sourceList = $(`.card[data-task-id="${taskId}"]`).closest('.list-cards');
        const targetList = $(this);
        
        if (sourceList.data('list-id') !== targetList.data('list-id')) {
            // Move card to new list
            const card = $(`.card[data-task-id="${taskId}"]`).detach();
            targetList.append(card);
            
            // Update task in backend
            updateTaskList(taskId, targetList.data('list-id'));
        }
        
        // Update positions
        updatePositions(sourceList);
        if (sourceList.data('list-id') !== targetList.data('list-id')) {
            updatePositions(targetList);
        }
    });
}

function updateTaskList(taskId, newListId) {
    $.ajax({
        url: `/api/tasks/${taskId}`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            listId: parseInt(newListId)
        })
    });
}

function updatePositions(listElement) {
    const listId = listElement.data('list-id');
    const cards = listElement.find('.card');
    
    cards.each(function(index) {
        const taskId = $(this).data('task-id');
        $(this).data('position', index);
        
        $.ajax({
            url: `/api/tasks/${taskId}`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                position: index
            })
        });
    });
}