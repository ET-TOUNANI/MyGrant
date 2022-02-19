package ma.my.grant.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ma.my.grant.R;
import ma.my.grant.models.Brother;
import ma.my.grant.utilities.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StudentBrothersAdapter extends RecyclerView.Adapter<StudentBrothersAdapter.ViewHolder> {

    private final Context context;
    private final List<Brother> list;

    public StudentBrothersAdapter(Context context, List<Brother> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_student_brother, parent, false);
        return new ViewHolder(context, v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(list.get(position));
        holder.delete.setTag(list.get(position));
        Brother element = list.get(position);
        holder.title.setText(element.getFirstName()+" "+element.getLastName());
        holder.description.setText(element.getBirthDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public Button delete;

        public ViewHolder(final Context context, View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            delete = itemView.findViewById(R.id.delete);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Brother element = (Brother) view.getTag();
                    deleteElement(element);
                }
            });


        }


        public void deleteElement(final Brother element) {
            try {
                String query = "DELETE FROM brothers WHERE id_b = ?";
                PreparedStatement preparedStatement = Database.connection.prepareStatement(query);
                preparedStatement.setInt(1, element.getId());
                int result = preparedStatement.executeUpdate();
                if (result == 1) {
                    int position = list.indexOf(element);
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, list.size());
                    Toast.makeText(context, "The operation completed successfully !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "An error occurred, please try again !", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
}
