import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private EditText taskEditText;
    private Button addTaskButton;
    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // initialize views
        taskEditText = findViewById(R.id.task_edit_text);
        addTaskButton = findViewById(R.id.add_task_button);
        taskListView = findViewById(R.id.task_list_view);

        // initialize Firebase Real-time Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");

        // set up task adapter for list view
        taskAdapter = new TaskAdapter(this, R.layout.task_item, new ArrayList<Task>(), databaseReference);
        taskListView.setAdapter(taskAdapter);

        // add task button click listener
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        // set up listener to listen for changes in the tasks node in the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear existing task list and add tasks from database
                taskAdapter.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    Task task = taskSnapshot.getValue(Task.class);
                    task.setId(taskSnapshot.getKey());
                    taskAdapter.add(task);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle database error
            }
        });
    }

    private void addTask() {
        String taskName = taskEditText.getText().toString();
        if (!TextUtils.isEmpty(taskName)) {
            String taskId = databaseReference.push().getKey();
            Task task = new Task(taskName);
            databaseReference.child(taskId).setValue(task);
            taskEditText.setText("");
        }
    }

    public class TaskAdapter extends ArrayAdapter<Task> {

        private LayoutInflater inflater;
        private int resourceId;
        private DatabaseReference databaseReference;

        public TaskAdapter(Context context, int resourceId, List<Task> taskList, DatabaseReference databaseReference) {
            super(context, resourceId, taskList);
            this.inflater = LayoutInflater.from(context);
            this.resourceId = resourceId;
            this.databaseReference = databaseReference;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(resourceId, parent, false);
            }
            Task task = getItem(position);
            TextView taskNameTextView = view.findViewById(R.id.task_name_text_view);
            taskNameTextView.setText(task.getName());
            Button deleteButton = view.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTask(task);
                }
            });
            return view;
        }

        private void deleteTask(Task task) {
            databaseReference.child(task.getId()).removeValue();
        }
    }
}