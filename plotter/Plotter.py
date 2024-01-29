import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

class Plotter:
    def __init__(self, filepath):
        self.data = pd.read_csv(filepath)

    def plot_danger_coefficient(self):
        plt.figure(figsize=(10, 6))
        sns.lineplot(data=self.data, x="iteration", y="dangerCoefficient", hue="district")
        plt.title("Danger Coefficient Over Time by District")
        plt.xlabel("Iteration")
        plt.ylabel("Danger Coefficient")
        plt.legend(title="District")
        plt.show()

    def plot_number_of_patrols(self):
        plt.figure(figsize=(10, 6))
        sns.barplot(data=self.data, x="district", y="numberOfPatrols", hue="district")
        plt.title("Number of Patrols by District")
        plt.xlabel("District")
        plt.ylabel("Number of Patrols")
        plt.legend(title="District")
        plt.show()

    def plot_number_of_interventions(self):
        plt.figure(figsize=(10, 6))
        sns.scatterplot(data=self.data, x="iteration", y="numberOfInterventions", hue="district")
        plt.title("Number of Interventions Over Time by District")
        plt.xlabel("Iteration")
        plt.ylabel("Number of Interventions")
        plt.legend(title="District")
        plt.show()
    
    def plot_neutralized_patrols(self):
        plt.figure(figsize=(10, 6))
        sns.lineplot(data=self.data, x="iteration", y="numberOfNeutralizedPatrols", hue="district")
        plt.title("Number of Neutralized Patrols Over Time by District")
        plt.xlabel("Iteration")
        plt.ylabel("Number of Neutralized Patrols")
        plt.legend(title="District")
        plt.show()

    def plot_pie_chart(self, column):
        sum_df = self.data.groupby('district')[column].sum()
        sum_df.plot(kind='pie', autopct='%1.1f%%', figsize=(8, 8))
        plt.title(f"Pie Chart for {column}")
        plt.ylabel('') 
        plt.show()

    def plot_relationship(self, x, y, title):
        plt.figure(figsize=(10, 6))
        sns.scatterplot(data=self.data, x=x, y=y, hue="district")
        plt.title(title)
        plt.xlabel(x)
        plt.ylabel(y)
        plt.legend(title="District")
        plt.show()

    def plot_threat_level(self):
        # Pivot the data
        pivot_data = self.data.pivot(index='iteration', columns='district', values='threatLevel')

        # Create the heatmap
        plt.figure(figsize=(10, 6))
        sns.heatmap(pivot_data)
        plt.title("Threat Level Over Iterations by District")
        plt.xlabel("District")
        plt.ylabel("Iteration")
        plt.show()


plotter = Plotter('../statistics/district_statistics.csv')
plotter.plot_danger_coefficient()
plotter.plot_number_of_patrols()
plotter.plot_number_of_interventions()
plotter.plot_pie_chart('numberOfInterventions')
plotter.plot_neutralized_patrols();

plotter.plot_relationship('numberOfInterventions', 'dangerCoefficient', 'Number of Interventions vs Danger Coefficient')
plotter.plot_relationship('numberOfFirings', 'dangerCoefficient', 'Number of Firings vs Danger Coefficient')
plotter.plot_relationship('numberOfInterventions', 'numberOfFirings', 'Number of Interventions vs Number of Firings')
