# Language Models Speed Up Local Search for Finding Programmatic Policies

Encoding policies that solve sequential decision-making problems as programs offer advantages over neural representations, such as interpretability and modifiability of the policies. On the downside, programmatic policies are elusive because their generation requires one to search in spaces of programs that are often discontinuous. In this paper, we leverage the ability of large language models (LLMs) to write computer programs to speed up the synthesis of programmatic policies. We use an LLM to provide initial candidates for the policy, which are then improved by local search. Empirical results in three problems that are challenging for programmatic representations show that LLMs can speed up local search and facilitate the synthesis of policies. We conjecture that LLMs are effective in this setting because we give them access to the outcomes of the policy rollouts. That way, LLMs can try policies encoding different behaviors, once they observe what a previous policy has accomplished. This process forces the search to explore different parts of the space through “exploratory initial programs”. Experiments also show that much of the knowledge LLMs leverage comes from the domain-specific language that defines the search space - the overall performance of the system drops sharply if we change the name of the functions used in the language to meaningless names. Since our system only queries the LLM in the first step of the search, it offers an economical method for using LLMs to guide the synthesis of policies.

![Screen Shot 2024-11-14 at 1 34 44 PM](https://github.com/user-attachments/assets/7da7a540-4abf-4dbf-8c1b-116993a41fcf)

### Usage

**To Build:** `javac -cp "lib/*:src" -d bin src/ai/synthesis/LocalSearch/Tests/MainTest.java`

**To Run:** `java -cp "lib/*:bin" ai.synthesis.LocalSearch.Tests.MainTest <map_id> <algorithm> <explain_dsl?>`

**Map:**
| ID  | Name |
| ------------- | ------------- |
| 0  | TwoBasesBarracks16x16  |
| 1  | basesWorkers24x24A  |
| 2  | basesWorkers32x32A  |
| 3  | BloodBath  |
| 4  | NoWhereToRun9x8  |
| 6  | DoubleGame24x24  |
| 7  | BWDistantResources32x32  |

**Algorithm:**
| ID  | Name |
| ------------- | ------------- |
| 0  | Iterated Best Response (IBR)  |
| 1  | Fictitious Play (FP)  |
| 2  | Local Learner (2L)  |
| 3  | IBR with LLM  |
| 4  | FP with LLM  |
| 5  | 2L with LLM  |
| 6  | DO with LLM  |
| 7  | Double Oracle (DO)  |
